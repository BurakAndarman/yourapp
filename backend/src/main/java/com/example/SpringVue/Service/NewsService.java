package com.example.SpringVue.Service;

import com.example.SpringVue.Dto.NewsApi.Everything.Everything;

public interface NewsService {

    Everything getEverything(String q, String language);

    Everything getEverything(String language);

}
