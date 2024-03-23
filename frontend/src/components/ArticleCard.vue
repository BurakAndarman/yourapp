<script setup>
    import default_image from '../assets/default_news_image.jpg';

    const article = defineProps({
        imageUrl: String,
        title: String,
        url: String,
        description: String,
        author: String
    });
</script>
<template>
    <v-card
      class="d-flex flex-column bg-background"
      width="400"
    >
      <v-img
        class="align-end text-white"
        height="200"
        gradient="to bottom, rgba(0,0,0,.1), rgba(0,0,0,.5)"
        :src="article.imageUrl || default_image"
        cover
      >
        <v-card-title>{{ article.title }}</v-card-title>
      </v-img>     
      <v-card-subtitle class="pt-4">
        {{ article.author || "Unknown Author" }}
      </v-card-subtitle>
      <v-card-text>
        <div>
          {{ article.description || article.title }}
        </div>
      </v-card-text>
      <v-spacer>
      </v-spacer>
      <v-card-actions>
        <v-btn :href="article.url" target="_blank" class="font-weight-bold" color="primary">
          Read More
        </v-btn>
      </v-card-actions>
    </v-card>
  </template>