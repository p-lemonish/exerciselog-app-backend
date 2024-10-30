package s24.backend.exerciselog.service;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

import java.util.*;

import s24.backend.exerciselog.domain.dto.RoleDto;
import s24.backend.exerciselog.domain.entity.CompletedWorkout;
import s24.backend.exerciselog.domain.entity.ExerciseLog;
import s24.backend.exerciselog.domain.entity.PlannedExerciseLog;
import s24.backend.exerciselog.domain.entity.Role;
import s24.backend.exerciselog.domain.entity.User;
import s24.backend.exerciselog.domain.entity.Workout;
import s24.backend.exerciselog.exception.ResourceNotFoundException;
import s24.backend.exerciselog.repository.*;
import s24.backend.exerciselog.util.SecurityUtils;

@Service
public class AdminService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PlannedExerciseLogService plannedExerciseLogService;
    @Autowired
    private WorkoutService workoutService;
    @Autowired
    private ExerciseLogRepository exerciseLogRepository;
    
    public void updateUserRole(Long id, RoleDto roleDto) throws BadRequestException {
        String roleName = roleDto.getRoleName();
            if(roleName == null || roleName == "") {
                throw new BadRequestException("Role name must not be empty");
            }
            User currentUser = SecurityUtils.getCurrentUser();
            User userToUpdate = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
            Role roleToSet = roleRepository.findByName(roleName).orElseThrow(() -> new ResourceNotFoundException("Role not found"));

            if (userToUpdate.getUsername().equals(currentUser.getUsername())) {
                throw new BadRequestException("You cannot change your own role");
            }

            userToUpdate.setRole(roleToSet);
            userRepository.save(userToUpdate);
    }

    @Transactional
    public void deleteUser(Long id) throws BadRequestException {
        User currentUser = SecurityUtils.getCurrentUser();
        User userToDelete = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (userToDelete.getUsername().equals(currentUser.getUsername())) {
            throw new BadRequestException("Cannot delete your own account");
        }

        // Delete all the user's Workout, CompletedWorkout, PlannedExerciseLog, and ExerciseLog entities
        List<Workout> workouts = new ArrayList<>(userToDelete.getWorkouts());
        for (Workout workout : workouts) {
            userToDelete.getWorkouts().remove(workout);
            workoutService.deletePlannedWorkout(workout.getId());
        }

        List<CompletedWorkout> completedWorkouts = new ArrayList<>(userToDelete.getCompletedWorkouts());
        for(CompletedWorkout completedWorkout : completedWorkouts) {
            userToDelete.getCompletedWorkouts().remove(completedWorkout);
            workoutService.deleteCompletedWorkout(completedWorkout.getId());
        }

        List<PlannedExerciseLog> plannedExerciseLogs = new ArrayList<>(userToDelete.getPlannedExerciseLogs());
        for(PlannedExerciseLog plannedExerciseLog : plannedExerciseLogs) {
            plannedExerciseLogService.deletePlannedExerciseLog(plannedExerciseLog.getId());
        }

        List<ExerciseLog> exerciseLogs = exerciseLogRepository.findByUser(userToDelete);
        for(ExerciseLog exerciseLog : exerciseLogs) {
            exerciseLog.setUser(null);
        }
        exerciseLogRepository.deleteAll(exerciseLogs);

        userRepository.deleteById(id);
    }
}
