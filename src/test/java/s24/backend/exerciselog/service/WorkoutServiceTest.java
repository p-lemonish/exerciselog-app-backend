package s24.backend.exerciselog.service;

import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.*;
import java.util.*;

import s24.backend.exerciselog.domain.dto.*;
import s24.backend.exerciselog.domain.entity.CompletedWorkout;
import s24.backend.exerciselog.domain.entity.ExerciseLog;
import s24.backend.exerciselog.domain.entity.PlannedExerciseLog;
import s24.backend.exerciselog.domain.entity.User;
import s24.backend.exerciselog.domain.entity.Workout;
import s24.backend.exerciselog.exception.*;
import s24.backend.exerciselog.mapper.*;
import s24.backend.exerciselog.repository.*;
import s24.backend.exerciselog.util.SecurityUtils;

@ExtendWith(MockitoExtension.class)
public class WorkoutServiceTest {
    
    @InjectMocks
    private WorkoutService workoutService;
    @Mock
    private WorkoutRepository workoutRepository;
    @Mock
    private CompletedWorkoutRepository completedWorkoutRepository;
    @Mock
    private ExerciseLogRepository exerciseLogRepository;
    @Mock
    private PlannedExerciseLogRepository plannedExerciseLogRepository;
    @Mock
    private ExerciseLogMapper exerciseLogMapper;
    @Mock
    private WorkoutMapper workoutMapper;
    @Mock
    private CompletedWorkoutMapper completedWorkoutMapper;
    @Mock
    private PlannedExerciseLogMapper plannedExerciseLogMapper;
    @Mock
    private User user;

    // BeforeEach means that the setUp() is called before every test-method gets called
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetUserWorkouts() {
        
        // Create list with Workout-entities
        List<Workout> workouts = Arrays.asList(new Workout(), new Workout());
        // Mock the JPA repository looking for a user's workouts
        when(workoutRepository.findByUser(user)).thenReturn(workouts); 

        // Create list with WorkoutDto-entities
        List<WorkoutDto> workoutDtos = Arrays.asList(new WorkoutDto(), new WorkoutDto());
        // Mock workoutMapper creating a list of workoutDtos from workouts
        when(workoutMapper.toDtoList(workouts)).thenReturn(workoutDtos); 

        // Let workoutService work with the given mocks
        List<WorkoutDto> result = workoutService.getUserWorkouts(user);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(workoutRepository, times(1)).findByUser(user);
        verify(workoutMapper, times(1)).toDtoList(workouts);
    }

    @Test
    public void testGetUserCompletedWorkouts() {

        List<CompletedWorkout> completedWorkouts = Arrays.asList(new CompletedWorkout(), new CompletedWorkout());
        when(completedWorkoutRepository.findByUser(user)).thenReturn(completedWorkouts);

        List<CompletedWorkoutDto> completedWorkoutDtos = Arrays.asList(new CompletedWorkoutDto(), new CompletedWorkoutDto());
        when(completedWorkoutMapper.toDtoList(completedWorkouts)).thenReturn(completedWorkoutDtos);

        List<CompletedWorkoutDto> result = workoutService.getUserCompletedWorkouts(user);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(completedWorkoutRepository, times(1)).findByUser(user);
        verify(completedWorkoutMapper, times(1)).toDtoList(completedWorkouts);
    }

    @Test
    public void testGetUserPlannedExercises() {

        List<PlannedExerciseLog> plannedExerciseLogs = Arrays.asList(new PlannedExerciseLog(), new PlannedExerciseLog());
        when(plannedExerciseLogRepository.findByUser(user)).thenReturn(plannedExerciseLogs);

        List<PlannedExerciseLogDto> plannedExerciseLogDtos = Arrays.asList(new PlannedExerciseLogDto(), new PlannedExerciseLogDto());
        when(plannedExerciseLogMapper.toDtoList(plannedExerciseLogs)).thenReturn(plannedExerciseLogDtos);

        List<PlannedExerciseLogDto> result = workoutService.getUserPlannedExercises(user);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(plannedExerciseLogRepository, times(1)).findByUser(user);
        verify(plannedExerciseLogMapper, times(1)).toDtoList(plannedExerciseLogs);
    }

    @Test
    public void testAddWorkout() {

        WorkoutDto workoutDto = new WorkoutDto();
        workoutDto.setSelectedExerciseIds(Arrays.asList(1L, 2L));

        List<PlannedExerciseLog> plannedExerciseLogs = Arrays.asList(new PlannedExerciseLog(), new PlannedExerciseLog());
        when(plannedExerciseLogRepository.findAllById(workoutDto.getSelectedExerciseIds())).thenReturn(plannedExerciseLogs);

        Workout workout = new Workout();
        when(workoutMapper.toEntity(workoutDto, user)).thenReturn(workout);

        workoutService.addWorkout(workoutDto, user);

        verify(plannedExerciseLogRepository, times(1)).findAllById(workoutDto.getSelectedExerciseIds());
        verify(workoutMapper, times(1)).toEntity(workoutDto, user);
        verify(workoutRepository, times(1)).save(workout);
    }

    @Test
    public void testStartWorkout() {

        Long workoutId = 1L;
        Workout workout = new Workout();
        workout.setId(workoutId);
        workout.setName("test");
        workout.setDate(LocalDate.now());

        PlannedExerciseLog plannedExerciseLog = new PlannedExerciseLog();
        plannedExerciseLog.setId(1L);
        List<PlannedExerciseLog> plannedExerciseLogs = Arrays.asList(plannedExerciseLog);
        workout.setPlannedExerciseLogs(plannedExerciseLogs);

        when(workoutRepository.findById(workoutId)).thenReturn(Optional.of(workout));

        List<ExerciseLogDto> exerciseLogDtos = Arrays.asList(new ExerciseLogDto());
        when(exerciseLogMapper.plannedExerciseLogListToDtoList(plannedExerciseLogs)).thenReturn(exerciseLogDtos);

        CompletedWorkoutDto result = workoutService.startWorkout(workoutId);

        assertNotNull(result);
        assertEquals(workout.getName(), result.getWorkoutName());
        assertEquals(workoutId, result.getId());
        verify(workoutRepository, times(1)).findById(workoutId);
        verify(exerciseLogMapper, times(1)).plannedExerciseLogListToDtoList(plannedExerciseLogs);
    }

    @Test
    public void testDeletePlannedWorkout() {

        Long workoutId = 1L;
        Workout workout = new Workout();
        workout.setId(workoutId);

        ExerciseLog exerciseLog1 = new ExerciseLog();
        ExerciseLog exerciseLog2 = new ExerciseLog();
        List<ExerciseLog> exerciseLogs = Arrays.asList(exerciseLog1, exerciseLog2);

        when(workoutRepository.findById(workoutId)).thenReturn(Optional.of(workout));
        when(exerciseLogRepository.findByWorkout(workout)).thenReturn(exerciseLogs);

        workoutService.deletePlannedWorkout(workoutId);

        verify(workoutRepository, times(1)).findById(workoutId);
        verify(exerciseLogRepository, times(1)).findByWorkout(workout);
        verify(workoutRepository, times(1)).delete(workout);
        assertNull(exerciseLog1.getWorkout());
        assertNull(exerciseLog2.getWorkout());
    }

    @Test
    public void testDeleteCompletedWorkout() {

        Long completedWorkoutId = 1L;
        CompletedWorkout completedWorkout = new CompletedWorkout();
        completedWorkout.setId(completedWorkoutId);

        when(completedWorkoutRepository.findById(completedWorkoutId)).thenReturn(Optional.of(completedWorkout));

        workoutService.deleteCompletedWorkout(completedWorkoutId);

        verify(completedWorkoutRepository, times(1)).findById(completedWorkoutId);
        verify(completedWorkoutRepository, times(1)).delete(completedWorkout);
    }

    @Test
    public void testCompleteWorkout_Success() throws Exception {

        Long workoutId = 1L;
        Workout workout = new Workout();
        workout.setId(workoutId);
        workout.setName("test");
        workout.setDate(LocalDate.now());

        when(workoutRepository.findById(workoutId)).thenReturn(Optional.of(workout));

        WorkoutDto workoutDto = new WorkoutDto();
        List<Long> selectedExerciseIds = Arrays.asList(1L, 2L);
        workoutDto.setSelectedExerciseIds(selectedExerciseIds);
        when(workoutMapper.toDto(workout)).thenReturn(workoutDto);

        CompletedWorkoutDto completedWorkoutDto = new CompletedWorkoutDto();
        completedWorkoutDto.setExercises(new ArrayList<>());
        ExerciseLogDto exerciseLogDto1 = new ExerciseLogDto();
        exerciseLogDto1.setExerciseId(1L);
        ExerciseLogDto exerciseLogDto2 = new ExerciseLogDto();
        exerciseLogDto2.setExerciseId(2L);
        completedWorkoutDto.getExercises().add(exerciseLogDto1);
        completedWorkoutDto.getExercises().add(exerciseLogDto2);

        CompletedWorkout completedWorkout = new CompletedWorkout();
        when(completedWorkoutMapper.toEntity(completedWorkoutDto)).thenReturn(completedWorkout);

        PlannedExerciseLog plannedExerciseLog1 = new PlannedExerciseLog();
        plannedExerciseLog1.setId(1L);
        PlannedExerciseLog plannedExerciseLog2 = new PlannedExerciseLog();
        plannedExerciseLog2.setId(2L);
        when(plannedExerciseLogRepository.findById(1L)).thenReturn(Optional.of(plannedExerciseLog1));
        when(plannedExerciseLogRepository.findById(2L)).thenReturn(Optional.of(plannedExerciseLog2));
            
        ExerciseLog exerciseLog1 = new ExerciseLog();
        ExerciseLog exerciseLog2 = new ExerciseLog();

        User currentUser = new User();
        currentUser.setId(1L);

        // Mock SecurityUtils-class to return the currentUser as created above
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUser).thenReturn(currentUser);

            when(exerciseLogMapper.toEntity(
                eq(exerciseLogDto1), 
                any(ExerciseLog.class), 
                eq(plannedExerciseLog1), 
                eq(currentUser), 
                eq(workout))
            )
            .thenReturn(exerciseLog1);

            when(exerciseLogMapper.toEntity(
                eq(exerciseLogDto2), 
                any(ExerciseLog.class), 
                eq(plannedExerciseLog2), 
                eq(currentUser), 
                eq(workout))
            )
            .thenReturn(exerciseLog2);

            workoutService.completeWorkout(workoutId, completedWorkoutDto);

            verify(workoutRepository, times(1)).findById(workoutId);
            verify(plannedExerciseLogRepository, times(1)).findById(1L);
            verify(plannedExerciseLogRepository, times(1)).findById(2L);
            verify(exerciseLogRepository, times(2)).save(any(ExerciseLog.class));
            verify(completedWorkoutRepository, times(1)).save(completedWorkout);
        }
    }

    @Test
    public void testCompleteWorkout_ExerciseIdsMismatch() {

        Long workoutId = 1L;
        Workout workout = new Workout();
        workout.setId(workoutId);

        when(workoutRepository.findById(workoutId)).thenReturn(Optional.of(workout));

        WorkoutDto workoutDto = new WorkoutDto();
        List<Long> selectedExerciseIds = Arrays.asList(1L, 2L);
        workoutDto.setSelectedExerciseIds(selectedExerciseIds);
        when(workoutMapper.toDto(workout)).thenReturn(workoutDto);

        CompletedWorkoutDto completedWorkoutDto = new CompletedWorkoutDto();
        completedWorkoutDto.setExercises(new ArrayList<>());
        ExerciseLogDto exerciseLogDto1 = new ExerciseLogDto();

        // Set bad ID for exerciseLogDto1 to cause problems
        exerciseLogDto1.setExerciseId(3L); 

        completedWorkoutDto.getExercises().add(exerciseLogDto1);

        User currentUser = new User();
        currentUser.setId(1L);

        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUser).thenReturn(currentUser);

            Exception exception = assertThrows(BadRequestException.class, () -> {
                workoutService.completeWorkout(workoutId, completedWorkoutDto);
            });

            assertEquals("SelectedExerciseIds and completedWorkoutDtoExerciseIds do not match", exception.getMessage());
        }
    }

    @Test
    public void testCompleteWorkout_WorkoutNotFound() {

        Long workoutId = 1L;
        when(workoutRepository.findById(workoutId)).thenReturn(Optional.empty());

        CompletedWorkoutDto completedWorkoutDto = new CompletedWorkoutDto();

        User currentUser = new User();
        currentUser.setId(1L);

        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUser).thenReturn(currentUser);

            Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
                workoutService.completeWorkout(workoutId, completedWorkoutDto);
            });

            assertEquals("Workout not found", exception.getMessage());
        }
    }
}
