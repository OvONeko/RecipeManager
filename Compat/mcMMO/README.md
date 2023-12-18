# MorePotions Compat mcMMO

This is a component of MorePotions added [mcMMO](https://mcmmo.org/) plugin compatibility.

## Build

### Step 1. Clone mcMMO repository

```shell
git clone https://github.com/mcMMO-Dev/mcMMO.git
```

### Step 2. Build mcMMO and install to mavenLocal

```shell
mvn install
```

### Step 3. Build MorePotions with Compat mcMMO

Windows:
```shell
set MORE_POTIONS_MCMMO=true
gradlew build
```

Linux:
```shell
export MORE_POTIONS_MCMMO=true
./gradlew build
```
