package ru.ikss.semver;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jetbrains.annotations.NotNull;

import ru.ikss.semver.pre_release.PreRelease;

public final class VersionBuilder {
    private static final String FULL = "full";
    private static final String BASE = "base";
    private static final String COMPARABLE = "comparable";
    private static final String MAJOR = "major";
    private static final String MINOR = "minor";
    private static final String PATCH = "patch";
    private static final String PRE_RELEASE = "preRelease";
    private static final String META_DATA = "metaData";
    //@formatter:off
    private static final Pattern PATTERN = Pattern.compile(
        "(?<full>" +
            "(?<comparable>" +
                "(?<base>" +
                    "(?<major>0|[1-9]+0*)" +
                    "(?:\\.(?<minor>0|[1-9]+0*))?" +
                    "(?:\\.(?<patch>0|[1-9]+0*))?" +
                ")" +
                "(?:-(?<preRelease>[\\da-z\\-]+(?:\\.[\\da-z\\-]+)*))?)" +
                "(?:\\+(?<metaData>[\\da-z\\-.]+)"+
            ")?" +
        ")", Pattern.CASE_INSENSITIVE | Pattern.COMMENTS
    );
    //@formatter:on

    public static boolean matches(@NotNull String version) {
        return PATTERN.matcher(version).matches();
    }

    public static Version build(@NotNull String fullVersion) {
        Matcher matches = PATTERN.matcher(fullVersion);
        if (matches.matches()) {
            try {
                return makeVersion(matches);
            } catch (NumberFormatException e) {
                throw new VersionFormatException(fullVersion, e);
            }
        }
        throw new VersionFormatException(fullVersion);
    }

    private static Version makeVersion(Matcher matches) {
        String major = matches.group(VersionBuilder.MAJOR);
        String minor = matches.group(VersionBuilder.MINOR);
        String patch = matches.group(VersionBuilder.PATCH);
        String preRelease = matches.group(VersionBuilder.PRE_RELEASE);
        String metaData = matches.group(VersionBuilder.META_DATA);
        return new Version(
            Integer.parseInt(major),
            minor == null ? 0 : Integer.parseInt(minor),
            patch == null ? 0 : Integer.parseInt(patch),
            preRelease == null ? PreRelease.EMPTY : new PreRelease(preRelease),
            metaData == null ? MetaData.EMPTY : new MetaData(metaData)
        );
    }
}
