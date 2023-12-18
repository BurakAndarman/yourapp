<script setup>
    import { useAuthStore } from '../store/auth';
    import { onMounted, ref } from 'vue';
    import ArticleCard from '../components/ArticleCard.vue';
    import NewsPreferences from '../components/NewsPreferences.vue';

    const auth = useAuthStore()
    const articles = ref([])
    const error = ref('')

    onMounted(async () => {

        try{

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
                error.value = parsedResponse.message;
            }

        }catch(e) {
            error.value = e;
        }


    })
</script>
<template>
    <div class="mx-auto" style="width:75%;">
        <div class="my-14 d-flex justify-space-between align-center">
            <div class="text-cyan-darken-4">
                <h1 class="text-h2">News</h1>
                <h2 class="text-h4 mt-3">Most Popular News Around the World</h2>
            </div>
            <div>
                <NewsPreferences>
                </NewsPreferences>
            </div>
        </div>
        <div class="my-10 d-flex flex-wrap justify-space-between ga-8">
            <ArticleCard
                v-for="(article, index) in articles"
                :imageUrl="article.urlToImage"
                :title="article.title"
                :url="article.url"
                :description="article.description"
                :author="article.author"
                :key="index"
            >
            </ArticleCard>
        </div>
    </div>    
</template>