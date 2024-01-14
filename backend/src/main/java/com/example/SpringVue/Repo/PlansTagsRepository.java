package com.example.SpringVue.Repo;

import com.example.SpringVue.Entity.Plans;
import com.example.SpringVue.Entity.PlansTags;
import com.example.SpringVue.Entity.Tags;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface PlansTagsRepository extends JpaRepository<PlansTags, Integer> {

    void deletePlansTagsByPlans(Plans plans);

    void deletePlansTagsByTagsNotInAndPlans(Collection<Tags> tags, Plans plans);

}
