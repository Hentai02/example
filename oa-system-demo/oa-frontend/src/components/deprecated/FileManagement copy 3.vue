<template>
  <div>
    <vue-finder id="my_vuefinder" :request="request"></vue-finder>
  </div>
</template>

<script setup>
const request = 'https://vuefinder.ozdemir.be/vuefinder?q=index&adapter=null'
</script>
