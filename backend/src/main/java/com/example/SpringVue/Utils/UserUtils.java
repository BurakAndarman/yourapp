package com.example.SpringVue.Utils;

import com.example.SpringVue.Dto.TagsDto;
import com.example.SpringVue.Entity.Plans;
import com.example.SpringVue.Entity.PlansTags;
import com.example.SpringVue.Entity.Tags;
import com.example.SpringVue.Repo.PlansTagsRepository;
import com.example.SpringVue.Repo.TagsRepository;
import com.example.SpringVue.Service.MediaService;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Component
public class UserUtils {

    private final MediaService mediaService;

    private final TagsRepository tagsRepository;

    private final PlansTagsRepository plansTagsRepository;

    public UserUtils(MediaService mediaService, TagsRepository tagsRepository, PlansTagsRepository plansTagsRepository) {
        this.mediaService = mediaService;
        this.tagsRepository = tagsRepository;
        this.plansTagsRepository = plansTagsRepository;

    }

    // Checks if passed tag exists in db. If exists, gets it and if not creates new one. Then adds a relation record.
    public void createNewPlansTagsRelation(Plans plan, TagsDto tagsDto) {

        Optional<Tags> tag = tagsRepository.findFirstByNameAndColor(tagsDto.getName(), tagsDto.getColor());

        Tags tagFromDb;
        if (tag.isEmpty()) {
            tagFromDb = tagsRepository.save(new Tags(
                    tagsDto.getName(),
                    tagsDto.getColor()
            ));
        } else {
            tagFromDb = tag.get();
        }

        plansTagsRepository.save(new PlansTags(
                plan,
                tagFromDb
        ));

    }

    public String savePlanImageToCloud(MultipartFile uploadedImage) {

        String image = "";

        try {
            image = mediaService.uploadFile(uploadedImage, "YourApp/Plans");
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }

        return image;

    }

    public void deletePlanImageFromCloud(String fileName) {

        try {
            mediaService.deleteFile(fileName, "YourApp/Plans");
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }

    }

}
