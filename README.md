# Air Traffic Simulator

A desktop application that simulates air traffic between airports, written in Java with a Swing
GUI. Built as the Object-Oriented Programming 2 course project at the School of Electrical
Engineering, University of Belgrade.

Airports and flights are entered through the GUI, stored in memory, and can be imported from and
exported to CSV and JSON. The map view draws the airports and animates the planes moving between
them in simulated time.

## Features

**Data entry.** Airports (name, unique three-letter code, coordinates in `[-180, 180] x [-90, 90]`)
and flights (origin, destination, departure time, duration in minutes) are entered through forms and
listed in tables. Every input is validated and every failure is reported through a dialog
that states what is wrong and how to fix it, rather than a generic error.

**Persistence.** Import and export in CSV and JSON, both handled behind a single `FileHandler`
interface. A failed import is rolled back, so a malformed file never leaves the application with
half-loaded data.

**Map.** Airports are drawn as squares labelled with their code; clicking one selects it and makes it
blink. The side panel filters which airports are visible, and the mouse wheel zooms the visible
range in and out.

**Simulation.** Time starts at 00:00 and runs at ten simulated minutes per real second, with
positions refreshed every 200 ms. Each airport clears at most one departure per ten simulated
minutes, so flights scheduled together queue up. Planes are drawn as silhouettes rotated to their
heading and move in a straight line to their destination. Selecting a plane and then an airport
redirects the plane there in mid-flight. The simulation can be started, paused and reset.

**Inactivity timeout.** The application closes after 60 seconds without user input; a dialog appears
for the last 5 seconds and lets the user stay. The countdown is suspended while the simulation runs
or while something on the map is selected.

## Architecture

The code is split into layers with the dependencies pointing inwards: the GUI knows about the
services, the services know about the model, and the model knows about nothing else.

```
src/
  airtraffic/          domain layer
    model/             Airport, Flight
    service/           AirportService, FlightService, DataService, change notification
    files/             FileHandler + CsvHandler, JsonHandler
    utils/             validation, geometry, airport lookup
    exception/         ValidationException, FileParseException
  simulation/          simulation layer
    SimulationClock    Swing timer driving simulated time
    SimulationEngine   owns the planes, applies airspace rules, emits events
    FlightController   runway scheduling (one departure per ten minutes per airport)
    Airplane           position and state of a single plane
    interfaces/        ClockListener, SimulationListener, AirspaceRule, AirplaneEvent
  gui/                 presentation layer
    dataPanel/         forms and tables for airports and flights
    mapPanel/          map view, airport filter, simulation controls, drawable components
    utils/             dialogs, inactivity timer
    Theme              colors, fonts and spacing in one place
```

Two extension points carry the design: `SimulationListener`, which lets any part of the GUI react to
takeoffs, landings and redirections without the engine knowing about the GUI, and `AirspaceRule`,
which lets new airspace behaviour be added without touching the engine. `Hurricane` is an example rule,
kept in the code but not registered by default: it draws itself on the map as an `Overlay` and diverts
every plane that enters it to the nearest alternative airport.

## Running

Requires Java 17 or newer. Gson is included in the repository as `gson-2.10.jar`.

```bash
javac -d out -cp gson-2.10.jar $(find src -name "*.java")
java -cp out:gson-2.10.jar Main
```

On Windows, use `;` instead of `:` as the classpath separator.

Sample data for both formats is in `data/`. Load it through *File -> Import CSV* or
*File -> Import JSON*.

## Data formats

CSV uses two sections, each with a header row:

```
# AIRPORTS
CODE,NAME,X,Y
BEG,Belgrade Nikola Tesla,10,45

# FLIGHTS
FROM,TO,DEPARTURE,DURATION
BEG,LHR,17:10,170
```

JSON uses one object with an `airports` array and an optional `flights` array:

```json
{
  "airports": [{"code":"BEG","name":"Belgrade Nikola Tesla","x":10,"y":45}],
  "flights":  [{"from":"BEG","to":"LHR","departure":"17:10","duration":170}]
}
```

The simulation writes a short line to the console for every event, which is useful when following
what the planes are doing:

```
[08:30] TAKEOFF    LHR -> JFK
[11:05] REDIRECTED LHR -> CDG
[13:22] LANDED     LHR -> CDG
```
