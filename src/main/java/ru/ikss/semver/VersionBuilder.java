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

    /**
     * Build {@link ru.ikss.semver.Version} from String template
     *
     * @param fullVersion
     *     String representation of version with format major.minor.patch[-preRelease][+metaData]
     *     <ul>
     *     <li>major :: 0|[1-9]+0*</li>
     *     <li>minor :: 0|[1-9]+0*</li>
     *     <li>patch :: 0|[1-9]+0*</li>
     *     <li>preRelease :: [id](\.[id])*</li>
     *     <li>id :: [0-9a-zA-Z\-]+</li>
     *     <li>metaData :: [0-9a-zA-Z\-]+</li>
     *     </ul>
     * @throws VersionFormatException
     *     when version isn't correct
     * @see <a href="http://semver.org/spec/v2.0.0.html">Semantic versioning details</a>
     */
    public static Version build(@NotNull String fullVersion) {
        Matcher matches = PATTERN.matcher(fullVersion);
        if (!matches.matches()) {
            throw new VersionFormatException(fullVersion);
        }
        try {
            return makeVersion(matches);
        } catch (NumberFormatException e) {
            throw new VersionFormatException(fullVersion, e);
        }
    }

    public static Version build(int major) {
        return build(major, 0, 0, null, null);
    }

    /**
     * Build {@link ru.ikss.semver.Version} from all elements
     *
     * @param major
     *     major version :: 0|[1-9]+0*
     * @param minor
     *     minor version :: 0|[1-9]+0*
     * @param patch
     *     patch version :: 0|[1-9]+0*
     * @param preRelease
     *     pre-release :: [[0-9a-zA-Z\-]+](\.[[0-9a-zA-Z\-]+])*
     * @param metaData
     *     metadata :: [0-9a-zA-Z\-]+
     * @throws VersionFormatException
     *     when version isn't correct
     * @see Version
     * @see <a href="http://semver.org/spec/v2.0.0.html">Semantic versioning details</a>
     */
    public static Version build(int major, int minor, int patch, String preRelease, String metaData) {
        StringBuilder version = new StringBuilder().append(major).append('.').append(minor).append('.').append(patch);
        if (preRelease != null && !preRelease.isEmpty()) {
            version.append('-').append(preRelease);
        }
        if (metaData != null && !metaData.isEmpty()) {
            version.append('+').append(metaData);
        }
        return build(version.toString());
    }

    public static Version build(@NotNull String comparable, String metaData) {
        String version = comparable;
        if (metaData != null && !metaData.isEmpty()) {
            version += '+' + metaData;
        }
        return build(version);
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
