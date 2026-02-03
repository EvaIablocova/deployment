package com.example.database_microservice.service;

import org.springframework.stereotype.Service;
import com.example.database_microservice.DTOs.TaskDTO;
import com.example.database_microservice.model.Task;
import com.example.database_microservice.repository.TaskRepository;
import com.example.database_microservice.repository.ProjectRepository;
import com.example.database_microservice.model.User;
import com.example.database_microservice.model.Project;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserService userService;
    private final ProjectRepository projectRepository;

    public TaskService(TaskRepository taskRepository, UserService userService, ProjectRepository projectRepository) {
        this.taskRepository = taskRepository;
        this.userService = userService;
        this.projectRepository = projectRepository;
    }

    // --- Mapping ---
    private TaskDTO toDTO(Task task) {
        return new TaskDTO(task);
    }

    // --- CRUD ---
    public List<TaskDTO> getAllTasks() {
//        var tasks = taskRepository.findAll();
        return taskRepository.findAll().stream().map(this::toDTO).toList();
//        return new ArrayList<>();
    }

    public Optional<TaskDTO> getTaskById(Long id) {
        return taskRepository.findById(id).map(this::toDTO);
    }

    public TaskDTO createTask(TaskDTO taskDTO) {
        Task task = new Task(taskDTO);

        if (taskDTO.getUserId() != null) {
            User user = new User(userService.getUserById(taskDTO.getUserId()).orElseThrow());
            task.setAssignedTo(user);
        }

        if (taskDTO.getProjectId() != null) {
            Project project = projectRepository.findById(taskDTO.getProjectId())
                    .orElseThrow(() -> new RuntimeException("Project not found with id: " + taskDTO.getProjectId()));
            task.setProject(project);
        }

        return toDTO(taskRepository.save(task));
    }


    public Optional<TaskDTO> updateTask(Long id, TaskDTO updatedTaskDTO) {

            return taskRepository.findById(id).map(task -> {
                // Update fields directly from DTO
                task.setTitle(updatedTaskDTO.getTitle());
                task.setDescription(updatedTaskDTO.getDescription());
                task.setDateToExecute(updatedTaskDTO.getDateToExecute());
                task.setDone(updatedTaskDTO.isDone());
                task.setPointsForCompletion(updatedTaskDTO.getPointsForCompletion());

                if (updatedTaskDTO.getUserId() != null) {
                    User user = userService.getUserEntityById(updatedTaskDTO.getUserId())
                            .orElseThrow(() -> new RuntimeException("User not found with id: " + updatedTaskDTO.getUserId()));
                    task.setAssignedTo(user);
                } else {
                    task.setAssignedTo(null);
                }

                if (updatedTaskDTO.getProjectId() != null) {
                    Project project = projectRepository.findById(updatedTaskDTO.getProjectId())
                            .orElseThrow(() -> new RuntimeException("Project not found with id: " + updatedTaskDTO.getProjectId()));
                    task.setProject(project);
                } else {
                    task.setProject(null);
                }

                return toDTO(taskRepository.save(task));
        });
    }

    public boolean deleteTask(Long id) {
        if (taskRepository.existsById(id)) {
            taskRepository.deleteById(id);
            return true;
        }
        return false;
    }

}
