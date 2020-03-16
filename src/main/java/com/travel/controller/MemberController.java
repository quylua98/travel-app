package com.travel.controller;

import com.travel.dto.PageResponse;
import com.travel.dto.PlanProfileRespone;
import com.travel.dto.UserForm;
import com.travel.dto.UserPageResponse;
import com.travel.entity.Plan;
import com.travel.entity.PlanInteractor;
import com.travel.entity.User;
import com.travel.exception.BadRequestException;
import com.travel.repository.PlanInteractorRepository;
import com.travel.repository.PlanRepository;
import com.travel.repository.UserRepository;
import com.travel.service.MemberService;
import com.travel.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@ControllerAdvice
@RequestMapping(value = "api/member")
public class MemberController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MemberController.class);

    private static final int NUM_DAY = -7;

    public static final int TOTAL_ROW_IN_PAGE = 10;

    @Autowired
    UserRepository userRepository;

    @Autowired
    MemberService memberService;

    @Autowired
    PlanRepository planRepository;

    @Autowired
    PlanInteractorRepository planInteractorRepository;

    @GetMapping(value = "/profile")
    public ResponseEntity<Object> getUserProfileFromToken() {
        Authentication au = SecurityContextHolder.getContext().getAuthentication();
        String email = au.getName();
        User user = userRepository.findByEmail(email).orElse(null);
        if (user != null) {
            UserForm userForm = new UserForm();
            userForm.setUsername(user.getUsername());
            userForm.setEmail(user.getEmail());
            userForm.setdOfB(user.getdOfB());
            userForm.setGender(user.isGender());
            userForm.setFullName(user.getFullName());
            return new ResponseEntity<>(userForm, HttpStatus.OK);
        }
        return new ResponseEntity<>(Constants.FAIL_TO_LOAD_USERDETAILS, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping(value = "/new-comer")
    public PageResponse getNewComers(Pageable pageable) {

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_WEEK, NUM_DAY);
        Date agoDate = calendar.getTime();
        Page<User> page =
                userRepository.findAllWithJoinDateAfter(agoDate, pageable);
        
        PageResponse<User> response = new PageResponse<User>();
        response.setCurrentPage(pageable.getPageNumber());
        response.setTotalPage(page.getTotalPages());
        response.setContent(page.getContent());

        return response;
    }

    /**
     * method get list my plan creat by me
     * @param id
     * @return
     */

    @GetMapping(value = "/{id}/myplan")
    public ResponseEntity getMyPlan(@PathVariable Long id) {

        List<Plan> myPlan = userRepository.findById(id).map(user -> user.getPlans()).orElseThrow(() -> new NullPointerException("User not found with id : " + id));
        //List<Plan> myPlan = user.getPlans();
        List<PlanProfileRespone> myPlanProfile = myPlan.stream()
                .map(p -> new PlanProfileRespone(p.getId(), p.getName(), p.getImageCover(), p.getPlanInteractors()
                        .size())).collect(Collectors.toList());

        return ResponseEntity.ok().body(myPlanProfile);
    }
    /**
     *  method getMyFollow.
     * @param id
     * @return
     */
    @GetMapping(value = "/{id}/follow")
    public ResponseEntity getMyFollow(@PathVariable Long id) {
        //User user = userRepository.findById(id).orElse(null);
        List<PlanInteractor> planInteractors = userRepository.findById(id).map(user1 -> user1.getPlanInteractors())
                .orElseThrow(() -> new NullPointerException("User not found with id : " + id) );
                    //return new ResponseEntity<>(Constants.USER_NOT_EXIST, HttpStatus.BAD_REQUEST); );
        //List<PlanInteractor> planInteractors = user.getPlanInteractors();

        List<Plan> planFollowList = planInteractors.stream()
                .filter(p -> p.isFollow() == true)
                .map(p -> p.getPlan())
                .sorted((p1, p2) -> p1.getCreatedDay().before(p2.getCreatedDay()) ? 1 : -1)
                .collect(Collectors.toList());

        List<PlanProfileRespone> listFollowPlan = planFollowList.stream()
                .map(p -> new PlanProfileRespone(p.getId(), p.getName(), p.getImageCover(), p.getPlanInteractors().size()))
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(listFollowPlan);
    }

    /**
     * method get list my joining plan
     * @param id
     * @return
     */

    @GetMapping(value = "/{id}/join")
    public ResponseEntity getMyJoin(@PathVariable Long id, Pageable pageable) {
        try{
            User user =  memberService.getMyJoin(id,pageable);
            List<PlanInteractor> planInteractors = user.getPlanInteractors();
            List<Plan> planJoinList = planInteractors.stream()
                    .filter(p -> p.isJoin() == true)
                    .map(PlanInteractor::getPlan)
                    .sorted((p1, p2) -> p1.getCreatedDay().before(p2.getCreatedDay()) ? 1 : -1)
                    .collect(Collectors.toList());

            List<PlanProfileRespone> listJoinPlan = planJoinList.stream()
                    .map(p -> p.convertMyPlan())  // cover Plan to PlanProfile
                    //.map(p -> new PlanProfileRespone(p.getId(), p.getName(), p.getImageCover(), p.getPlanInteractors().size()))
                    .collect(Collectors.toList());

            int start = (int) pageable.getOffset();
            int end = (start + pageable.getPageSize()) > listJoinPlan.size() ? listJoinPlan.size() : (start + pageable.getPageSize());

            Page page = new PageImpl<>(listJoinPlan.subList(start, end), pageable, listJoinPlan.size());
            PageResponse<PlanProfileRespone> planPage = new PageResponse<PlanProfileRespone>(pageable.getPageNumber(),
                    page.getTotalPages(), page.getContent());

            return ResponseEntity.ok().body(planPage);
        }catch (BadRequestException e){
            LOGGER.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getLocalizedMessage());
            //return ResponseEntity.status(e.getHttpStatus()).body(e.getMessage());

//            return ResponseEntity.badRequest()
//                    .body(new HashMap<String,String>().put("Error",e.getMessage()));
        }

    }

}
