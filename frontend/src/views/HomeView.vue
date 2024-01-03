<script setup>
    import { useAuthStore } from '../store/auth';
    import { useStatusStore } from '../store/status';
    import { onMounted, ref } from 'vue';
    import ArticleCard from '../components/ArticleCard.vue';
    import NewsPreferences from '../components/NewsPreferences.vue';

    const auth = useAuthStore()
    const statusDialog = useStatusStore()
    const articles = ref([])

    onMounted(async () => {

        try{

            await fetchArticles()

        }catch(e) {

            statusDialog.openStatusDialog(e,'error')

        }

    })

    const fetchArticles = async () => {
        const response = await fetch('http://localhost:8090/api/v1/user/get-news',{
            method : "GET",
            headers : {
                "Authorization" : `Bearer ${auth.token}`
            }
        });

        if(response.status == 401) {
            auth.logout();
        }

        const parsedResponse = await response.json();

        if(response.status == 200) {
            articles.value = parsedResponse;
        
        } else {
            throw new Error(parsedResponse.message)
        }
    }
</script>
<template>
    <div class="mx-auto" style="width:75%;">
        <div class="my-14 d-flex justify-space-between align-center">
            <div class="text-cyan-darken-4">
                <h1 class="text-h2">News</h1>
                <h2 class="text-h4 mt-3">Most Popular News Around the World</h2>
            </div>
            <div>
                <NewsPreferences :fetchNewArticles="fetchArticles"/>
            </div>
        </div>
        <div v-if="articles.length" class="my-10 d-flex flex-wrap justify-space-between ga-8">
            <ArticleCard
                v-for="(article, index) in articles"
                :imageUrl="article.urlToImage"
                :title="article.title"
                :url="article.url"
                :description="article.description"
                :author="article.author"
                :key="index"
            />
        </div>
        <div v-else class="mt-10 d-flex justify-center">
            <div class="d-flex flex-column justify-center align-center ga-3">
                <v-progress-circular color="cyan-darken-4" indeterminate :size="50"></v-progress-circular>
                <p class="text-cyan-darken-4">Loading</p>
            </div>           
        </div>
    </div>    
</template>