# Building AuraPay

## Prerequisites

- Java Development Kit (JDK) 11 or higher
- Apache Maven 3.6.0 or higher
- Git

## Step-by-Step Build Instructions

### 1. Clone Repository

```bash
git clone https://github.com/Kshitij-xtreme/AuraPay-Blockchain-Inspired-Campus-Transaction-Ledger.git
cd AuraPay-Blockchain-Inspired-Campus-Transaction-Ledger
```

### 2. Verify Java Installation

```bash
java -version
# Output should be Java 11 or higher
```

### 3. Build Project

#### Full Build
```bash
mvn clean install
```

#### Skip Tests
```bash
mvn clean install -DskipTests
```

#### Compile Only
```bash
mvn compile
```

### 4. Run Tests

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=TransactionTest

# Run with coverage
mvn test jacoco:report
```

### 5. Run Application

#### Option A: Maven
```bash
mvn exec:java
```

#### Option B: Java Command
```bash
java -cp target/aurapy-ledger-1.0.0.jar com.aurapy.AuraPayMain
```

#### Option C: Using JAR
```bash
mvn package
java -jar target/aurapy-ledger-1.0.0.jar
```

## Troubleshooting Build Issues

### Issue: "mvn command not found"
**Solution**: Install Maven or add it to PATH

### Issue: "Java version mismatch"
**Solution**: Update pom.xml to match your Java version

### Issue: Compilation errors
**Solution**: 
```bash
mvn clean
mvn compile
```

### Issue: Dependency download failures
**Solution**: Check internet connection and Maven repository

## IDE Setup

### IntelliJ IDEA
1. Open project
2. File → Project Structure → Project SDK (select Java 11+)
3. Build → Build Project

### Eclipse
1. File → Import → Maven → Existing Maven Projects
2. Select project directory
3. Finish

### VS Code
1. Install Extension Pack for Java
2. Open project folder
3. Maven extension will detect pom.xml

## Development Workflow

```bash
# 1. Create feature branch
git checkout -b feature/new-feature

# 2. Make changes
# ... edit files ...

# 3. Build and test
mvn clean test

# 4. Commit
git add .
git commit -m "Add new feature"

# 5. Push
git push origin feature/new-feature
```

## Performance Optimization

### Build Optimization
```bash
# Parallel builds
mvn -T 1C clean install

# Offline mode (after first build)
mvn -o install

# Skip javadoc
mvn install -DskipTests -Dspeed
```

## Packaging

### Create Executable JAR
```bash
mvn clean package shade:shade
```

### Create Distribution
```bash
mvn clean package assembly:single
```

## Deployment

### Local Deployment
```bash
mvn install
```

### Remote Repository
```bash
mvn deploy
```

## Continuous Integration

For GitHub Actions, add `.github/workflows/maven.yml`:

```yaml
name: Java CI

on: [push, pull_request]

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
    - uses: actions/checkout@v2
    - uses: actions/setup-java@v2
      with:
        java-version: '11'
    - run: mvn clean install
```

## Documentation

### Generate JavaDoc
```bash
mvn javadoc:javadoc
# Output: target/site/apidocs/index.html
```

## Common Maven Commands

```bash
mvn clean              # Remove build directories
mvn compile            # Compile source code
mvn test              # Run tests
mvn package           # Create JAR
mvn install           # Install to local repository
mvn deploy            # Deploy to remote repository
mvn site              # Generate website
mvn help:describe     # Describe a plugin
```

## Troubleshooting Checklist

- [ ] Java version is 11+
- [ ] Maven is installed and in PATH
- [ ] pom.xml is in project root
- [ ] Internet connection available
- [ ] .m2 directory has write permissions
- [ ] No other process using same port
- [ ] Sufficient disk space
- [ ] No conflicting Java versions

## Support

If build fails:
1. Check error message carefully
2. Run `mvn clean install` fresh
3. Check Maven documentation
4. Create GitHub issue with:
   - Full error output
   - Java version
   - Maven version
   - Operating system
