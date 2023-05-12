<script setup lang="ts">
import { RouterLink, RouterView } from 'vue-router'
</script>

<template>
  <header>

    <div class="wrapper">
      <nav>
        <RouterLink to="/">Home</RouterLink>
        <RouterLink to="/write">글작성</RouterLink>
      </nav>
    </div>
  </header>

  <RouterView />
</template>

<style>

</style>