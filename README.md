# Bolt for Java Template App

This is a generic Bolt for Java template app used to build out Slack apps.

Before getting started, make sure you have a development workspace where you have permissions to install apps. If you don’t have one setup, go ahead and [create one](https://slack.com/create).

## Installation

#### Create a Slack App

1. Open [https://api.slack.com/apps/new](https://api.slack.com/apps/new) and choose "From an app manifest"
2. Choose the workspace you want to install the application to
3. Copy the contents of [manifest.json](./manifest.json) into the text box that says `*Paste your manifest code here*` (within the JSON tab) and click *Next*
4. Review the configuration and click *Create*
5. Click *Install to Workspace* and *Allow* on the screen that follows. You'll then be redirected to the App Configuration dashboard.

#### Environment Variables

Before you can run the app, you'll need to store some environment variables.

1. Open your apps configuration page from this list, click **OAuth & Permissions** in the left hand menu, then copy the Bot User OAuth Token. You will store this in your environment as `SLACK_BOT_TOKEN`.
2. Click ***Basic Information** from the left hand menu and follow the steps in the App-Level Tokens section to create an app-level token with the `connections:write` scope. Copy this token. You will store this in your environment as `SLACK_APP_TOKEN`.

```zsh
# Replace with your app token and bot token
export SLACK_BOT_TOKEN=<your-bot-token>
export SLACK_APP_TOKEN=<your-app-token>
```

### Setup Your Local Project

```zsh
# Clone this project onto your machine
git clone https://github.com/slackapi/java-python-template.git

# Change into this project directory
cd bolt-java-template
```

<details><summary><h4>Maven: Run</h4></summary>
<div>

Ensure [maven](https://maven.apache.org/index.html) in installed on your local environment.
* We recommend using [brew to install maven on mac](https://formulae.brew.sh/formula/maven)

```zsh
# Make sure you have maven installed
# Install the dependencies and compile
mvn clean compile

# Start your local server
mvn exec:java -Dexec.mainClass="Main"
```

**NOTE**: If you chose to use Maven as build tool you can remove the `builde.gradle` file from this project.

</div>
</details>
<details><summary><h4>Gradle: Run</h4></summary>
<div>

Ensure [gradle](https://gradle.org/) in installed on your local environment.
* We recommend using [brew to install gradle on mac](https://formulae.brew.sh/formula/gradle)

```zsh
# Make sure you have gradle installed
# Run tests
gradle test

# Start your local server
gradle run
```
**NOTE**: If you chose to use Gradle as build tool you can remove the `pom.xml` file from this project.

</div>
</details>

## Project Structure

### `manifest.json`

`manifest.json` is a configuration for Slack apps. With a manifest, you can create an app with a pre-defined configuration, or adjust the configuration of an existing app.

### `Main.java`

`./src/main/java/Main.java` is the entry point for the application and is the file you'll run to start the server. This project aims to keep this file as thin as possible, primarily using it as a way to route inbound requests.

### `/listeners`

Every incoming request is routed to a "listener". Inside this directory, we group each listener based on the Slack Platform feature used, so `/listeners/shortcuts` handles incoming [Shortcuts](https://api.slack.com/interactivity/shortcuts) requests, `/listeners/views` handles [View submissions](https://api.slack.com/reference/interaction-payloads/views#view_submission) and so on.

### `resources/logback.xml`

[`logback-classic`](https://mvnrepository.com/artifact/ch.qos.logback/logback-classic) is imported as a dependency to configure the logs of the project, this configuration is defined in a [logback.xml](https://logback.qos.ch/manual/configuration.html) found `src/main/resources/logback.xml`. Note that by default the project should be logging `debug` level logs for slack dependencies.

<details><summary><h2>App Distribution / OAuth</h2></summary>
<div>

Only implement OAuth if you plan to distribute your application across multiple workspaces. A separate `OauthMain.java` file can be found with relevant OAuth settings.

When using OAuth, Slack requires a public URL where it can send requests. In this template app, we've used [`ngrok`](https://ngrok.com/download). Checkout [this guide](https://ngrok.com/docs#getting-started-expose) for setting it up.

Start `ngrok` to access the app on an external network and create a redirect URL for OAuth.

```
ngrok http 3000
```

This output should include a forwarding address for `http` and `https` (we'll use `https`). It should look something like the following:

```
Forwarding   https://3cb89939.ngrok.io -> http://localhost:3000
```

Navigate to **OAuth & Permissions** in your app configuration and click **Add a Redirect URL**. The redirect URL should be set to your `ngrok` forwarding address with the `slack/events/oauth_redirect` path appended. For example:

```
https://3cb89939.ngrok.io/slack/events/oauth_redirect
```
</div>
</details>

*NOTE:* if you do not require Oauth you can remove all `OAUTH DEPENDENCIES` in the `pom.xml` or `build.gradle` files, along with `src/main/java/OauthMain.java`
