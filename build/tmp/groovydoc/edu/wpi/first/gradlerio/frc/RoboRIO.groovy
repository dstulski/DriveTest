package edu.wpi.first.gradlerio.frc

import groovy.transform.CompileStatic
import jaci.gradle.deploy.context.DeployContext
import jaci.gradle.deploy.target.RemoteTarget
import jaci.gradle.deploy.target.location.SshDeployLocation
import org.apache.log4j.Logger
import org.gradle.api.Project

import java.util.regex.Matcher
import java.util.regex.Pattern

@CompileStatic
class RoboRIO extends RemoteTarget {

    private Logger log;

    RoboRIO(String name, Project project) {
        super(name, project)
        log = Logger.getLogger(this.toString())

        this.directory = '/home/lvuser'

        this.maxChannels = 4

        this.onlyIf = { DeployContext ctx ->
            verifyOnlyIf(ctx)
        }
    }

    int team
    void setTeam(int team) {
        this.team = team
        setAddresses("roborio-${team}-FRC.local".toString(), "10.${(int)(team / 100)}.${team % 100}.2".toString(), "172.22.11.2")
    }

    void setAddresses(String... addresses) {
        this.locations.clear()
        addresses.each { String addr ->
            this.getLocations().location(SshDeployLocation) { SshDeployLocation loc ->
                loc.setAddress(addr)
                loc.setIpv6(false)
                loc.setUser("admin")
                loc.setPassword("")
            }
        }

        this.getLocations().location(DSDeployLocation) { DSDeployLocation ds ->
            ds.setUser("admin")
            ds.setPassword("")
            ds.setIpv6(false)
        }
    }

    boolean verifyOnlyIf(DeployContext ctx) {
        ctx.logger.silent(true)
        if (checkImage) {
            log.info("Checking image...")
            readAndVerifyImage(ctx);
        }
        return true
    }

    void readAndVerifyImage(DeployContext context) {
        final String imageFile = "/etc/natinst/share/scs_imagemetadata.ini"
        final Pattern pattern = Pattern.compile("^IMAGEVERSION\\s=\\s\\\"FRC_roboRIO_([0-9]{4}_v[0-9]{2,})\\\"")

        String content = context.execute("cat ${imageFile}")
        log.info("Received Image File: ")
        log.info(content)

        boolean imageFound = false
        content.split("\n").each { String line ->
            Matcher matcher = pattern.matcher(line.trim())
            if (matcher.matches()) {
                String imageGroup = matcher.group(1)
                log.info("Matched version: " + imageGroup)
                verifyImageVersion(imageGroup)
                imageFound = true
            }
        }

        if (!imageFound) {
            throw new InvalidImageException()
        }
    }

    boolean checkImage = true
    List<String> validImageVersions = ["2018_v16", "2018_v17", "2018_v18"]

    void verifyImageVersion(String image) {
        if (!validImageVersions.contains(image))
            throw new InvalidImageException(image, validImageVersions)
    }

    @Override
    String toString() {
        return "RoboRIO[${name}]".toString()
    }
}
