# Model Railway Routing Application

A Java application for automated routing and control of locomotives on a Märklin digital model railway with Central
Station 3 integration.
This application provides path planning, locomotive control, and real-time position tracking through a command-line
interface.

## Prerequisites

- Java JDK 23 or newer
- Märklin Central Station 3 (CS3)
- [Modelleisenbahn-Websteuerung](https://github.com/Rediate15/Modelleisenbahn-Websteuerung) backend services running
- Gradle for building

## Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/Integraluminium/Modellbahn-Routing.git
   cd Modellbahn-Routing
   ```

2. **Build with Gradle**
   ```bash
   ./gradlew build
   ```

3. **Create executable JAR**
   ```bash
   ./gradlew jar
   ```

## Features

- **Automated Route Planning**: Create routes for locomotives using algorithms like Dijkstra
- **Position Tracking**: Track locomotive positions using feedback sensors (S88)
- **Switch Control**: Automatic control of switches during route execution
- **Speed Management**: Handles acceleration, maximum speed, and deceleration
- **Real-time Communication**: WebSocket integration with Märklin CS3
  over [Modelleisenbahn-Websteuerung](https://github.com/Rediate15/Modelleisenbahn-Websteuerung)
- **Scripting Support**: Run predefined command sequences from script files
- **Interactive REPL**: Command-line interface for real-time interaction

#### Route Calculation

The routing engine supports single and multiple locomotive path planning with these capabilities:

- **Path Finding**: Automatically calculates the shortest path from current position to destination
    - If direct routing isn't possible, the system attempts to reverse the locomotive
    - If reversing the locomotive could reach the destination, the system route to a buffer stop first to reverse, then
      to the final destination.

- **Collision Avoidance**: By default, the system tracks locomotive positions during routing
    - Occupied track sections are blocked for other locomotives to prevent collisions
    - This behavior can be disabled with `MODIFY AUTOMATIC ADD LOCOMOTIVES TO ROUTE false`

- **Directional Control**:
    - You can specify a required facing direction at the destination with `TO W13 FACING W12`
    - If no facing direction is specified, the system chooses the most efficient approach

- **Algorithm Selection**:
    - Uses Dijkstra's algorithm by default for shortest path calculation
    - Alternative algorithms can be specified with the `WITH` keyword (e.g., `WITH Bellman_Ford`)
    - Routing constraints can be added with `CONSIDER` and the optional flags `ELECTRIFICATION` and `HEIGHT`

#### Route Execution

- **Current Implementation**: Routes are executed using a timing-based approach at fixed speeds
    - The system calculates movement time based on distance and locomotive specifications
    - Switches are automatically set before the locomotive reaches them

- **Planned Improvements**:
    - Enhanced speed control with proper acceleration and deceleration phases
    - Integration with S88 feedback contacts for real-time position verification
    - Dynamic speed adjustments based on track conditions and signal states

## Configuration

### Track Layout

Track configuration is stored in YAML files in the [src/main/resources/config/track/*](src/main/resources/config/track)
directory:

every file is a list of track elements with its specific properties.
The Name is the unique identifier for the track element and is used to reference it in the route planning.
The ID is the identifier which is provided for the element by the Central Station.
The connections of the elements are referenced by the name of the connected element.

Example switch configuration:

```yaml
- name: W3
  id: 12290
  root: K6
  straight: K3
  turnout: W4
```

### Locomotives

Locomotives are described in [src/main/resources/config/locs/locs.yaml](src/main/resources/config/locs/locs.yaml).:

The ID is the identifier which is provided for the locomotive by the Central Station.
The Name is the name of the locomotive.
Electric is a boolean value that indicates if the locomotive is electric and is affected if the routing should only use
electrified Track.
Speed, accelerationTime, accelerationDistance, decelerationTime, and decelerationDistance are the properties of the
locomotive and are obtained experimentally.

The position is the name of the track element where the locomotive is currently located and Facing is the next
Graphpoint, where the locomotive is looking to.

```yaml
- id: 16397
  name: "150 144-4 DB AG"
  electric: true
  maxSpeed: 0.59
  accelerationTime: 2960
  accelerationDistance: 713
  decelerationTime: 2960
  decelerationDistance: 713
  position: "S1"
  facingDirection: "W6"
```

[//]: # (## Starting the WebSocket Backend)

[//]: # ()

[//]: # ()

[//]: # ()

[//]: # (First, start the required Modelleisenbahn-Websteuerung services:)

[//]: # ()

[//]: # ()

[//]: # ()

[//]: # (```powershell)

[//]: # ()

[//]: # (Start-Process -FilePath ".venv\Scripts\python.exe" -ArgumentList "backend/src/start.py", "raw_can_sender")

[//]: # ()

[//]: # (Start-Process -FilePath ".venv\Scripts\python.exe" -ArgumentList "backend/src/start.py", "can_sender")

[//]: # ()

[//]: # (Start-Process -FilePath ".venv\Scripts\python.exe" -ArgumentList "backend/src/start.py", "raw_can_receiver")

[//]: # ()

[//]: # (Start-Process -FilePath ".venv\Scripts\python.exe" -ArgumentList "backend/src/start.py", "can_receiver")

[//]: # ()

[//]: # (Start-Process -FilePath ".venv\Scripts\python.exe" -ArgumentList "backend/src/start.py", "can")

[//]: # ()

[//]: # (```)

## Usage

### Interactive Mode as REPL

Run the application without arguments to start in interactive mode:

```bash
java -jar build/libs/<NAME>.jar
```

Or with additional debug information:

```bash
java -jar build/libs/<NAME>.jar --debug
```

### Script Mode

Execute commands from a script file:

```bash
java -jar build/libs/<NAME>.jar scripts/myScript.moba
```

## Command Reference

The application supports these command types:

### System Commands

```
SYSTEM START        # Start the system
SYSTEM STOP         # Stop the system
```

### List Commands

```
LIST LOCOMOTIVES    # Show all locomotives
LIST GRAPHPOINTS    # Show all track points
LIST ROUTE          # Show current route if previously calculated
```

### Locomotive Control

```
MODIFY 16397 POSITION W17 FACING W16  # Set locomotive position
MODIFY 16397 SPEED 50                 # Set locomotive speed
MODIFY 16397 TOGGLE DIRECTION         # Toggle locomotive direction
```

### Route Planning

```
NEW ROUTE
    ADD 16397 AT W12 FACING W13 TO W13 FACING W12 USING DISTANCE
    CONSIDER ELECTRIFICATION HEIGHT
    WITH Dijkstra
```

### Execution

```
DRIVE               # Execute the defined route
```

One example Script can be found
in: [src/main/resources/scripts/sampleScript.moba](src/main/resources/scripts/sampleScript.moba)

## Troubleshooting

### Common Issues

- **WebSocket Connection Failed**: Ensure all Modelleisenbahn-Websteuerung backend services are running
- **Commands Not Recognized**: Check [grammar](src/main/resources/Backus–Naur-format.txt) of command
- **Locomotives Not Moving**: Verify CS3 is connected, powered on and system is started by using the button on the CS3.
  Check if rails are powered.
- **Route Planning Errors**: Ensure track layout is properly configured

