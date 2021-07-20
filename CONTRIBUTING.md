# Contributing to Pulp

## Running Pulp

We use the Gradle build system for running Pulp.

```
./gradlew run
```

## Modifying Pulp

If you want to submit your changes for later, make a new branch:

```
git checkout -b feature/<feature_name>
```

And start editing the files located in `src/`

## Creating a PR for your changes

After you have changed some code, you'll need to make a new git commit with:

```
git add -A
git commit -m "Short Description of Changes"
```

Next, push the changes on your branch to GitHub:

```
git push -u origin feature/<feature_name>
```

And click the given URL to create a PR on GitHub.

This PR will go under review by the QA for the week and will also be run under the CI for testing.
