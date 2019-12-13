# Quarkus JAX-RS REST Client Issue

## Reproduce

1. Build a native image ```mvn package -Pnative```
2. Start application ```./target/restclient-native-1.0.0-SNAPSHOT-runner```
3. Open URL <http://localhost:8080/hello>
4. Look at the warnings in the log-output
