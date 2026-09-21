# MongoDB setup

All four services connect to `mongodb://localhost:27017/` by default. Start your
local MongoDB server before starting the services. `MONGODB_URI` can override the
connection URI; `SPRING_MONGODB_DATABASE` can override a service's database name.

| Service | Database | Implemented collections |
| --- | --- | --- |
| driver-vehicle-service | ridelink_driver | drivers, vehicles |
| account-service | ridelink_account | None yet |
| ride-service | ridelink_ride | None yet |
| fare-payment-service | ridelink_payment | None yet |

MongoDB creates databases when data is first written. The three starter services
will not appear as populated databases until their persistence features are implemented.

In MongoDB Compass, connect using `mongodb://localhost:27017/`, refresh the database
list, and open `ridelink_driver` to view the `drivers` and `vehicles` collections.

## Run and test

From a service directory in PowerShell:

```powershell
.\mvnw.cmd spring-boot:run
.\mvnw.cmd test
```

The driver/vehicle API runs at `http://localhost:8082`. Its endpoints are unchanged.
IDs (`id`, `driverId`, and `accountId`) are now JSON strings. Newly created drivers
and vehicles receive MongoDB-generated IDs. Use the ID returned by the driver API
when creating a vehicle, for example:

```json
{
  "driverId": "<id returned by POST /api/drivers>",
  "registrationNumber": "CAB-5678",
  "vehicleType": "CAR",
  "brand": "Toyota",
  "model": "Prius",
  "colour": "White"
}
```

Vehicle registration numbers have a unique database index. `driverId` is indexed
for lookups. MongoDB integration tests require the local server and create a
unique `ridelink_driver_test_*` database, which they remove after the tests.
The existing service unit tests use Mockito.

## Existing H2 data

The local migration imported all existing records: one driver and one vehicle.
The original numeric IDs and references were preserved as strings (for example,
`1` became `"1"`). All imported fields were read back and compared with H2.
No account, ride, or payment records existed in this workspace.

The original `driver-vehicle-service/data/driverdb.mv.db` is retained. An additional
backup is at `driver-vehicle-service/data/driverdb.before-mongodb.mv.db`.
These local database files are excluded from Git.

`tools/src/main/java/com/ridelink/tools/MigrateH2ToMongo.java` is a standalone,
repeatable importer with its dependencies declared in `tools/pom.xml`, so IDEs
can resolve its MongoDB imports. Stop any old
H2-based service before running it. It opens H2 read-only, checks ID and registration
conflicts before writing, preserves driver references, skips identical documents,
and verifies imported fields. It never overwrites conflicting MongoDB records.
A failed or interrupted import can be rerun to import the remaining records.

To run from the repository root with the locally cached migration dependencies:

```powershell
$cache = Join-Path $env:USERPROFILE '.m2/repository'
$migrationClasspath = @(
  "$cache/com/h2database/h2/2.4.240/h2-2.4.240.jar"
  "$cache/org/mongodb/bson/5.6.2/bson-5.6.2.jar"
  "$cache/org/mongodb/mongodb-driver-core/5.6.2/mongodb-driver-core-5.6.2.jar"
  "$cache/org/mongodb/mongodb-driver-sync/5.6.2/mongodb-driver-sync-5.6.2.jar"
) -join ';'

# Preview: reads H2 and MongoDB without writing.
java --class-path $migrationClasspath tools/src/main/java/com/ridelink/tools/MigrateH2ToMongo.java driver-vehicle-service/data/driverdb mongodb://localhost:27017/ ridelink_driver

# Import and verify (creates a backup before the first import).
java --class-path $migrationClasspath tools/src/main/java/com/ridelink/tools/MigrateH2ToMongo.java driver-vehicle-service/data/driverdb mongodb://localhost:27017/ ridelink_driver --apply
```

These jar versions were present in the local Maven cache during migration. On a
different machine, supply paths to installed compatible H2 and MongoDB driver jars.
The application itself no longer depends on H2.
