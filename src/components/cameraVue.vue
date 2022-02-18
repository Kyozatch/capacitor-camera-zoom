<template>
  <div class="content">
    <video ref="videoTag" class="main-video" playsinline autoplay></video>
    <button @click="initializeVideoStream">Init Video</button>
    <input type="range" />
  </div>
</template>

<script>
export default {
  name: 'CameraVue',
  methods: {
    initializeVideoStream() {
      const constraints = {
        audio: true,
        video: { width: 1280, height: 720, zoom: true },
      };
      navigator.mediaDevices
        .getUserMedia(constraints)
        .then(function (mediaStream) {
          const track = mediaStream.getVideoTracks()[0];
          const capabilities = track.getCapabilities();
          const settings = track.getSettings();
          const input = document.querySelector('input[type="range"]');
          console.log(capabilities);
          console.log(settings);

          var video = document.querySelector('video');
          video.srcObject = mediaStream;
          video.onloadedmetadata = function () {
            video.play();
          };

          input.min = capabilities.zoom.min;
          input.max = capabilities.zoom.max;
          input.step = capabilities.zoom.step;
          input.value = settings.zoom;
          input.oninput = function (event) {
            track.applyConstraints({
              advanced: [{ zoom: parseInt(event.target.value) }],
            });
          };
          input.hidden = false;
        })
        .catch(function (err) {
          console.log(err.name + ': ' + err.message);
        });
    },
  },
};
</script>

<style scoped>
video {
  width: 70%;
  background: rgba(0, 0, 0, 0.2);
}
</style>
