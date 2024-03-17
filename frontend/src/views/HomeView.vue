<script setup>
    import { useAuthStore } from '../store/auth';
    import { useStatusStore } from '../store/status';
    import { onMounted, ref } from 'vue';
    import ArticleCard from '../components/ArticleCard.vue';
    import NewsPreferences from '../components/NewsPreferences.vue';

    const auth = useAuthStore()
    const statusDialog = useStatusStore()
    const articlesCategorized = ref({})
    const topicIconMap = {
        "science" : "brain",
        "technology" : "laptop",
        "sports" : "basketball",
        "health" : "leaf",
        "entertainment" : "ferris-wheel",
        "business" : "briefcase"
    }

    onMounted(async () => {

        try{

            await fetchArticles()

        }catch(e) {

            statusDialog.openStatusDialog(e,'error')

        }

    })

    const fetchArticles = async () => {
        const response = await fetch('http://localhost:8090/api/v1/user/news',{
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
           articlesCategorized.value = parsedResponse;
        
        } else {
            throw new Error(parsedResponse.message)
        }
    }
</script>
<template>
    <div class="bg-surface py-14">
        <div class="mx-auto" style="width:75%;">
            <div class="d-flex justify-space-between align-center">
                <div class="text-primary">
                    <h1 class="text-h2">News</h1>
                    <h2 class="text-h4 mt-3">Most Popular News Around the World</h2>
                </div>
                <div>
                    <NewsPreferences :fetchNewArticles="fetchArticles"/>
                </div>
            </div>
            <div v-if="Object.keys(articlesCategorized).length !== 0" class="my-10">
                <div v-for="(articles, topic, index) in articlesCategorized" :key="index">
                    <div class="d-flex justify-center align-center my-16" v-if="topic != 'general'">
                        <div class="w-100 ms-16">
                            <hr class="topic-hr bg-primary"/>
                        </div>                    
                        <h3 class="text-h5 font-weight-bold text-primary mx-10 d-flex align-center ga-4">
                            <v-icon :icon="'mdi-'+topicIconMap[topic]"></v-icon>
                            <div>{{ topic.charAt(0).toUpperCase() + topic.slice(1) }}</div>
                        </h3>
                        <div class="w-100 me-16">
                            <hr class="topic-hr bg-primary"/>
                        </div>
                    </div>
                    <div class="d-flex flex-wrap justify-space-between ga-8">
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
                </div>
            </div>
            <div v-else class="mt-10 h-screen">
                <div class="d-flex flex-column justify-center align-center ga-3">
                    <v-progress-circular color="primary" indeterminate :size="50"></v-progress-circular>
                    <p class="text-primary">Loading</p>
                </div>           
            </div>
        </div> 
    </div>   
</template>
<style scoped>
.topic-hr{
    border:none;
    height:3px;
}
</style>