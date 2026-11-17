<template>
  <div class="forbidden">
    <h1>403 - Forbidden</h1>
    <p>抱歉，您没有权限访问此页面。</p>
  </div>
</template>

<script>
export default {
  name: 'ForbiddenView',
}
</script>

<style scoped>
.forbidden {
  text-align: center;
  padding: 50px;
  font-size: 24px;
}
</style>
