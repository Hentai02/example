<template>
  <div class="flex min-h-screen bg-white">
    <!-- Sidebar -->

    <ASide />

    <!-- Main content -->
    <main class="flex-1 p-6">
      <router-view></router-view>
    </main>
  </div>
</template>

<script setup>
import ASide from '../views/ASideView.vue'
</script>
