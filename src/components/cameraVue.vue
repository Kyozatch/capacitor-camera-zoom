<template>
  <div class="content">
    <video
      ref="videoTag"
      class="main-video"
      playsinline
      autoplay
    ></video>
    <input type="range">
  </div>
</template>

<script>
export default {
  name: 'CameraVue',
  mounted() {
    this.initializeVideoStream();
  },
  methods: {
   initializeVideoStream() {
      const constraints = { audio: true, video: { width: 1280, height: 720, zoom: true} };
      navigator.mediaDevices.getUserMedia(constraints)
      .then(function(mediaStream) {
        const track = mediaStream.getVideoTracks()[0];
        const capabilities = track.getCapabilities();
        const settings = track.getSettings();
        const input = document.querySelector('input[type="range"]')
        console.log(capabilities);
        console.log(settings);

        var video = document.querySelector('video');
        video.srcObject = mediaStream;
        video.onloadedmetadata = function() {
          video.play();
        };

        input.min = 1;
        input.max = 4;
        input.step = 1;
        input.value = settings.zoom;
        input.oninput = function(event) {
          console.log('zoom set to: ', event.target.value)
          track.applyConstraints({advanced: [ {zoom: parseInt(event.target.value)} ]});
        }
        input.hidden = false;
      })
      .catch(function(err) { console.log(err.name + ": " + err.message); });
    },
},
}

</script>

<style scoped>
video {
    width: 70%;
    background: rgba(0, 0, 0, 0.2);
}
</style>
