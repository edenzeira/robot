# 🤖 SPL Assignment 2 – GurionRock Robot Mapping System

This Java project simulates a multi-threaded perception and mapping system for a robotic vacuum, developed in the SPL 225 course at Ben-Gurion University.

## 🎯 Goals
- Practice Java **concurrency**, **thread safety**, and **synchronization**
- Implement a **microservices framework** using message-passing
- Integrate data from sensors (Camera, LiDAR, GPS/IMU) into a global map via **Fusion-SLAM**
- Apply **event-driven architecture** and the **Message Loop Design Pattern**

## 🧠 Core Components

### Microservices
- `CameraService` – Sends detected object events  
- `LiDarWorkerService` – Processes detection into coordinates  
- `PoseService` – Sends the robot's pose  
- `FusionSlamService` – Builds and updates the environment map  
- `TimeService` – Broadcasts system ticks

### Messaging System
- `MessageBusImpl` – Thread-safe singleton for communication  
- Supports **Events** and **Broadcasts**  
- Uses **Round-Robin** scheduling for event dispatch

### Data Flow
1. Camera detects object → sends `DetectObjectsEvent`
2. LiDAR responds with coordinates → `TrackedObjectsEvent`
3. Pose is updated via `PoseEvent`
4. FusionSLAM integrates all → updates map

## ⚙️ How to Run

1. Compile using Maven:
```bash
mvn clean install
```

2. Run with config:
```bash
java -jar target/assignment2.jar path/to/config.json
```

3. Output will be written to `output_file.json` with:
- Final map (landmarks)
- System statistics
- Error snapshot (if applicable)

## 🧪 Test Files
Sample input files are available:
- `example_input/` – base configuration
- `example_input_with_error/` – includes sensor failure
- `example_input_2/` – larger scenario

## 🧱 Tech Stack
- Java 8
- Maven
- GSON (for JSON parsing)
- JUnit (for unit testing)
- Thread-safe design with minimal locking

## ✅ Status
✔️ Full system simulation  
✔️ Concurrency and synchronization implemented  
✔️ JUnit tests included  
✔️ Runs on CS lab UNIX machines  
✔️ No deadlocks, no memory leaks

---

