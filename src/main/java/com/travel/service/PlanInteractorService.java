package com.travel.service;

import com.travel.entity.Plan;
import com.travel.entity.PlanInteractor;
import com.travel.entity.User;
import com.travel.repository.PlanInteractorRepository;
import com.travel.repository.PlanRepository;
import com.travel.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class PlanInteractorService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PlanRepository planRepository;

    @Autowired
    private PlanInteractorRepository planInteractorRepository;

    public Plan updateFollowPlanInteractor(long planId, long userId, boolean isFollow) throws IllegalArgumentException {
        PlanInteractor planInteractor;
        User user = new User(userId);
        Plan plan = planRepository.findById(planId).orElse(null);
        Optional<PlanInteractor> opt = planInteractorRepository.findByPlanAndUser(plan,user);
        if(opt.isPresent()){
            planInteractor = opt.get();
            planInteractor.setFollow(isFollow);
            planInteractorRepository.save(planInteractor);
        }else{
            planInteractor = new PlanInteractor();
            planInteractor.setUser(user);
            planInteractor.setPlan(plan);
            planInteractor.setFollow(isFollow);
            planInteractorRepository.save(planInteractor);
        }
        return plan;
    }

}
