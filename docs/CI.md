# CI

### Build
A Maven build is run for each pull request with the [build action](../.github/workflows/build.yml)

### Release
The release is done **for every push on the main branche** with the [release action](../.github/workflows/release-and-publish.yml)

The following steps are executed
* The stable maven version is created
* The Maven artifacts are built
* The Git tag and the Github release are created
* The publication on Maven Central step is run based on the following secrets : 
  * *PUBLISH_ON_MAVEN_CENTRAL*: **value must be true or this step is not run**
  * *OSSRH_USERNAME*: Your username for issues.sonatype.com
  * *OSSRH_PASSWORD*: Your password for issues.sonatype.com
  * *GPG_SIGNING_KEY*: The public key published to the key servers
  * *GPG_PASSPHRASE*: The passphrase used to generate the public key