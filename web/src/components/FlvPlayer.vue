<template>
  <video id="videoElement" controls="controls"
         style="width:100%;height:100%;object-fit: fill;margin:auto; left: 0; right: 0; bottom: 0; top: 0;"></video>
</template>

<script>
import flvjs from 'flv.js'

export default {
  name: 'FlvPlayer',
  data() {
    return {
      mediaUrl: '',
      player: null
    }
  },
  props: {
    flvUrl: {
      type: String
    }
  },
  mounted() {
    this.initPlayer();
  },
  destroyed() {
    if (this.player) {
      this.player.destroy();
    }
  },
  methods: {
    initPlayer() {
      const app = this;
      if (flvjs.isSupported()) {
        const videoElement = document.getElementById('videoElement');
        if (app.player) {
          app.player.destroy();
          app.player = null;
        }
        const flvPlayer = flvjs.createPlayer({
          type: 'flv',
          isLive: true,
          url: app.flvUrl
        });
        flvPlayer.attachMediaElement(videoElement);
        flvPlayer.load();
        flvPlayer.play();
        app.player = flvPlayer;
      }
    }
  }
}
</script>
