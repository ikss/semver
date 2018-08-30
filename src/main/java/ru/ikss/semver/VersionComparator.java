package ru.ikss.semver;

import java.util.Comparator;
import java.util.List;

import ru.ikss.semver.pre_release.PreReleaseId;

final class VersionComparator {
    private VersionComparator() {}

    private static final Comparator<Version> PRE_RELEASE_COMPARATOR = (v1, v2) -> {
        List<PreReleaseId> first = v1.getPreRelease().getIdentifiers();
        List<PreReleaseId> second = v2.getPreRelease().getIdentifiers();
        int firstSize = first.size();
        int secondSize = second.size();
        if (firstSize == secondSize && firstSize == 0) {
            return 0;
        } else if (firstSize == 0) {
            return 1;
        } else if (secondSize == 0) {
            return -1;
        }
        for (int i = 0; i < Math.max(firstSize, secondSize); i++) {
            if (firstSize <= i) {
                return -1;
            } else if (secondSize <= i) {
                return 1;
            }
            int result = first.get(i).compareTo(second.get(i));
            if (result != 0) {
                return result;
            }
        }
        return 0;
    };

    static final Comparator<Version> SEMVER = Comparator.comparing(Version::getMajor)
        .thenComparing(Version::getMinor)
        .thenComparing(Version::getPatch)
        .thenComparing(PRE_RELEASE_COMPARATOR);
}
