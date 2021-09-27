package fun.bookish.blueberry.server.permission;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fun.bookish.blueberry.core.exception.ManualRollbackException;
import fun.bookish.blueberry.core.utils.DateUtils;
import fun.bookish.blueberry.core.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.security.Key;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Base64;

public class PermissionCodeUtils {

    private static final String SALT = "b2Xniz10XUCikZPE";

    /**
     * 偏移变量，固定占8位字节
     */
    private final static String IV_PARAMETER = "12345678";
    /**
     * 密钥算法
     */
    private static final String ALGORITHM = "DES";
    /**
     * 加密/解密算法-工作模式-填充模式
     */
    private static final String CIPHER_ALGORITHM = "DES/CBC/PKCS5Padding";
    /**
     * 默认编码
     */
    private static final String CHARSET = "utf-8";
    /***
     * 加密8位密码
     */
    private final static String PASSWORD = "0qelft03";
    /**
     * 生成key
     *
     * @param password
     * @return
     * @throws Exception
     */
    private static Key generateKey(String password) throws Exception {
        DESKeySpec dks = new DESKeySpec(password.getBytes(CHARSET));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
        return keyFactory.generateSecret(dks);
    }


    /**
     * DES加密字符串
     *
     * @param data 待加密字符串
     * @return 加密后内容
     */
    private static String encrypt(String data) throws Exception{
        if (data == null)
            return null;
        Key secretKey = generateKey(PASSWORD);
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        IvParameterSpec iv = new IvParameterSpec(IV_PARAMETER.getBytes(CHARSET));
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
        byte[] bytes = cipher.doFinal(data.getBytes(CHARSET));
        return new String(Base64.getEncoder().encode(bytes));
    }

    /**
     * DES解密字符串
     *
     * @param data 待解密字符串
     * @return 解密后内容
     */
    private static String decrypt(String data) throws Exception{

        if (data == null)
            return null;
        Key secretKey = generateKey(PASSWORD);
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        IvParameterSpec iv = new IvParameterSpec(IV_PARAMETER.getBytes(CHARSET));
        cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
        return new String(cipher.doFinal(Base64.getDecoder().decode(data.getBytes(CHARSET))), CHARSET);
    }

    public static void check(String code) {
        try {
            if (StringUtils.isBlank(code)) {
                throw new IllegalStateException();
            }
            String decrypt = decrypt(code);
            JsonObject jsonObject = JsonUtils.decodeToJsonObject(decrypt);
            String salt = jsonObject.get("salt").getAsString();
            if (!SALT.equals(salt)) {
                throw new IllegalStateException();
            }
            String endTime = jsonObject.get("endTime").getAsString();
            if (DateUtils.parseStrToLocalDate(endTime, "yyyy-MM-dd").isBefore(LocalDate.now())) {
                throw new ManualRollbackException("授权码已过期，请联系系统服务商");
            }
        } catch (Exception e) {
            if (e instanceof ManualRollbackException) {
                throw new IllegalStateException(e.getMessage());
            } else {
                throw new IllegalStateException("授权码非法，请联系系统服务商");
            }
        }
    }

    public static void main(String[] args) throws Exception {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("salt", SALT);
        jsonObject.addProperty("endTime", "2099-12-31");
        String encrypt = encrypt(jsonObject.toString());
        System.out.println("授权码：" + encrypt);
    }

}
