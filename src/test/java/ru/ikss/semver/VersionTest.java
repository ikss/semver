package ru.ikss.semver;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class VersionTest {

    @Test
    @DisplayName("Short versions")
    public void shortVersion() {
        assertEquals(Version.of("1.0.0"), Version.of("1"));
        assertEquals(Version.of("1.0.0"), Version.of("1.0"));
        assertEquals(Version.of("1.2.0"), Version.of("1.2"));
        assertEquals(Version.of("1.0.0-alpha"), Version.of("1.0-alpha"));
        assertEquals(Version.of("1.1.0-alpha"), Version.of("1.1-ALPhA"));

        assertEquals(Version.of("9.1.2"), Version.of(9, 1, 2));
        assertEquals(Version.of("9.1"), Version.of(9, 1));
        assertEquals(Version.of("9"), Version.of(9));
    }

    @Test
    @DisplayName("Short int versions")
    public void shortIntVersion() {
        assertEquals(Version.of("9.1.2"), Version.of(9, 1, 2));
        assertEquals(Version.of("19.1.0"), Version.of(19, 1));
        assertEquals(Version.of("9.0.0"), Version.of(9));
    }

    @Test
    @DisplayName("PreRelease version")
    public void preReleaseTest() {
        assertFalse(Version.of("1.0.1").isPreRelease());
        assertFalse(Version.of("1.0.1+develop6f3d8ae2").isPreRelease());
        assertTrue(Version.of("1.0.1-rc.1").isPreRelease());
    }

    @Test
    @DisplayName("Release version")
    public void releaseTest() {
        assertTrue(Version.of("1.0.1").isRelease());
        assertTrue(Version.of("1.0.1+develop6f3d8ae2").isRelease());
        assertFalse(Version.of("1.0.1-rc.1").isRelease());
    }
}
