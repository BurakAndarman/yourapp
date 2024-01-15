package com.example.SpringVue.Repo;

import com.example.SpringVue.Entity.Plans;
import com.example.SpringVue.Entity.PlansTags;
import com.example.SpringVue.Entity.Tags;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Repository
public interface PlansTagsRepository extends JpaRepository<PlansTags, Integer> {

    Set<PlansTags> getPlansTagsByTagsInAndPlans(Collection<Tags> tags, Plans plans);


}
