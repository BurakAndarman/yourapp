package com.example.SpringVue.Service.Impl;

import com.example.SpringVue.Dto.PlansDto;
import com.example.SpringVue.Dto.TagsDto;
import com.example.SpringVue.Entity.Plans;
import com.example.SpringVue.Entity.PlansTags;
import com.example.SpringVue.Entity.Tags;
import com.example.SpringVue.Entity.User;
import com.example.SpringVue.Repo.PlansRepository;
import com.example.SpringVue.Repo.PlansTagsRepository;
import com.example.SpringVue.Repo.TagsRepository;
import com.example.SpringVue.Service.MediaService;
import com.example.SpringVue.Service.PlansService;
import com.example.SpringVue.Service.UserService;
import com.example.SpringVue.Utils.KanbanList;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
public class PlansServiceImpl implements PlansService {

    private final PlansRepository plansRepository;

    private final PlansTagsRepository plansTagsRepository;

    private final TagsRepository tagsRepository;

    private final UserService userService;

    private final MediaService mediaService;

    public PlansServiceImpl(PlansRepository plansRepository,
                            PlansTagsRepository plansTagsRepository,TagsRepository tagsRepository,
                            UserService userService,MediaService mediaService) {
        this.plansRepository = plansRepository;
        this.plansTagsRepository = plansTagsRepository;
        this.tagsRepository = tagsRepository;
        this.userService = userService;
        this.mediaService = mediaService;
    }

    @Override
    public List<PlansDto> getPlans(String userName) {

        User user = userService.getUser(userName);

        List<Plans> plans = plansRepository.findAllByUser(user);

        List<PlansDto> plansDtos = plans.stream().map(userPlan -> {

            List<TagsDto> tagsDtoList = new ArrayList<>();

            if(userPlan.getPlansTags() != null) {
                tagsDtoList.addAll(userPlan.getPlansTags().stream().map(plansTags -> {

                    Tags tag = plansTags.getTags();

                    return new TagsDto(
                            tag.getId(),
                            tag.getName(),
                            tag.getColor()
                    );
                }).toList());
            }


            return new PlansDto(
                    userPlan.getId(),
                    userPlan.getTitle(),
                    userPlan.getContent(),
                    userPlan.getImage(),
                    userPlan.getKanbanList().toString(),
                    tagsDtoList
            );
        }).toList();

        return plansDtos;
    }

    @Transactional
    @Override
    public String savePlans(List<PlansDto> plansDtoList, List<MultipartFile> images, String userName) {

        User user = userService.getUser(userName);

        plansDtoList.stream().forEach(plansDto -> {
            if(plansDto.isCreated() && !plansDto.isDeleted()) {

                String image = "";
                String imgPublicId = "";

                if(plansDto.getImageIndex() != null) {
                    HashMap<String,String> cloudResponse = this.savePlanImageToCloud(images.get(plansDto.getImageIndex()));
                    image = cloudResponse.get("image_url");
                    imgPublicId = cloudResponse.get("public_id");
                }

                Plans newPlan = plansRepository.save(new Plans(
                        plansDto.getTitle(),
                        plansDto.getContent(),
                        image,
                        imgPublicId,
                        KanbanList.valueOf(plansDto.getKanbanList()),
                        user
                ));

                plansDto.getTags().stream().forEach(tagsDto -> this.createNewPlansTagsRelation(newPlan, tagsDto));

            } else if (plansDto.isChanged() && !plansDto.isDeleted()) {

                Plans oldPlan = plansRepository.findById(plansDto.getId()).get();

                String image = oldPlan.getImage();
                String imgPublicId = oldPlan.getImgPublicId();

                if(plansDto.getImageIndex() != null) {
                    if(!image.isEmpty()) {
                        this.deletePlanImageFromCloud(imgPublicId);
                    }

                    HashMap<String,String> cloudResponse = this.savePlanImageToCloud(images.get(plansDto.getImageIndex()));
                    image = cloudResponse.get("image_url");
                    imgPublicId = cloudResponse.get("public_id");

                } else if(plansDto.getImage().isEmpty() && !image.isEmpty()) {
                    this.deletePlanImageFromCloud(imgPublicId);

                    image = "";
                    imgPublicId = "";
                }

                Set<PlansTags> existingPlansTagsRelations = new HashSet<>();

                if(!plansDto.getTags().isEmpty()) {

                    Collection<Tags> existingOldTags = new ArrayList<>();

                    plansDto.getTags().stream().forEach(tagsDto -> {
                        if(!tagsDto.isCreated()) {
                            existingOldTags.add(tagsRepository.findById(tagsDto.getId()).get());
                        }
                    });

                    existingPlansTagsRelations = plansTagsRepository.getPlansTagsByTagsInAndPlans(existingOldTags,oldPlan);

                }

                Plans updatedPlan = plansRepository.save(new Plans(
                        plansDto.getId(),
                        plansDto.getTitle(),
                        plansDto.getContent(),
                        image,
                        imgPublicId,
                        KanbanList.valueOf(plansDto.getKanbanList()),
                        user,
                        existingPlansTagsRelations
                ));

                plansDto.getTags().stream().forEach(tagsDto -> {
                    if(tagsDto.isCreated()) {
                        this.createNewPlansTagsRelation(updatedPlan, tagsDto);
                    }
                });


            } else if (plansDto.isDeleted() && !plansDto.isCreated()) {

                Plans planToBeDeleted = plansRepository.findById(plansDto.getId()).get();

                if(!planToBeDeleted.getImage().isEmpty()) {
                    this.deletePlanImageFromCloud(planToBeDeleted.getImgPublicId());
                }

                plansRepository.delete(planToBeDeleted);

            }
        });

        return "Changes recorded successfully";
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

    public HashMap<String, String> savePlanImageToCloud(MultipartFile uploadedImage) {

        HashMap<String,String> uploadResponse;

        try {
            uploadResponse = mediaService.uploadFile(uploadedImage, "YourApp/Plans");

        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }

        return uploadResponse;

    }

    public void deletePlanImageFromCloud(String publicId) {

        try {
            mediaService.deleteFile(publicId);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }

    }

}
