package uk.q3c.util.version;

import org.junit.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * Using JUnit rather than Spock becuase constructor uses default param values
 * <p>
 * Created by David Sowerby on 24 Aug 2017
 */
public class VersionTest {

    VersionNumber version0 = new VersionNumber();
    VersionNumber version0a = new VersionNumber();
    VersionNumber version0rc1 = new VersionNumber(0, 0, 0, 0, "rc1");
    VersionNumber version0rc2 = new VersionNumber(0, 0, 0, 0, "rc2");
    VersionNumber version1 = new VersionNumber(1);
    VersionNumber version0_1 = new VersionNumber(0, 1);
    VersionNumber version0_0_1 = new VersionNumber(0, 0, 1);
    VersionNumber version0_0_2 = new VersionNumber(0, 0, 2);
    VersionNumber version0_0_0_1 = new VersionNumber(0, 0, 0, 1);
    VersionNumber version0_0_0_2 = new VersionNumber(0, 0, 0, 2);
    VersionNumber version0_0_0_1bx = new VersionNumber(0, 0, 0, 1, "", "buildX");
    VersionNumber version0_0_0_1by = new VersionNumber(0, 0, 0, 1, "", "buildY");


    @Test
    public void defaultConstructor() {
        // given
        VersionNumber version0 = new VersionNumber();

        // expect
        assertThat(version0.getMajor()).isEqualTo(0);
        assertThat(version0.getMinor()).isEqualTo(0);
        assertThat(version0.getPatch()).isEqualTo(0);
        assertThat(version0.getNonFunctional()).isEqualTo(0);
        assertThat(version0.getBuildMetaData()).isEqualTo("");
        assertThat(version0.getQualifier()).isEqualTo("");
    }

    @Test
    public void isSamesAs() {
        // given


        // expect identical to match
        assertThat(version0).isEqualTo(version0a);
        assertThat(version0.hashCode()).isEqualTo(version0a.hashCode());
        assertThat(version0.isSameVersionAs(version0a)).isTrue();
        assertThat(version0.isSameBaseVersionAs(version0a)).isTrue();
        assertThat(version0.isSameFunctionalVersionAs(version0a)).isTrue();
        assertThat(version0.isSameBuildAs(version0a)).isTrue();

        // expect qualifier difference to fail equals, and all except baseVersion
        assertThat(version0).isNotEqualTo(version0rc1);
        assertThat(version0.isSameVersionAs(version0rc1)).isFalse();
        assertThat(version0.isSameBaseVersionAs(version0rc1)).isTrue();
        assertThat(version0.isSameFunctionalVersionAs(version0rc1)).isFalse();
        assertThat(version0.isSameBuildAs(version0rc1)).isFalse();


        // major version different fail all
        assertThat(version0).isNotEqualTo(version1);
        assertThat(version0.isSameVersionAs(version1)).isFalse();
        assertThat(version0.isSameBaseVersionAs(version1)).isFalse();
        assertThat(version0.isSameFunctionalVersionAs(version1)).isFalse();
        assertThat(version0.isSameBuildAs(version1)).isFalse();

        // minor version different fail all
        assertThat(version0).isNotEqualTo(version0_1);
        assertThat(version0.isSameVersionAs(version0_1)).isFalse();
        assertThat(version0.isSameBaseVersionAs(version0_1)).isFalse();
        assertThat(version0.isSameFunctionalVersionAs(version0_1)).isFalse();
        assertThat(version0.isSameBuildAs(version0_1)).isFalse();

        // patch version different fail all
        assertThat(version0).isNotEqualTo(version0_0_1);
        assertThat(version0.isSameVersionAs(version0_0_1)).isFalse();
        assertThat(version0.isSameBaseVersionAs(version0_0_1)).isFalse();
        assertThat(version0.isSameFunctionalVersionAs(version0_0_1)).isFalse();
        assertThat(version0.isSameBuildAs(version0_0_1)).isFalse();

        // nonFunctional version different fail all except sameFunctionalVersion
        assertThat(version0).isNotEqualTo(version0_0_0_1);
        assertThat(version0.isSameVersionAs(version0_0_0_1)).isFalse();
        assertThat(version0.isSameBaseVersionAs(version0_0_0_1)).isFalse();
        assertThat(version0.isSameFunctionalVersionAs(version0_0_0_1)).isTrue();
        assertThat(version0.isSameBuildAs(version0_0_0_1)).isFalse();

        // build different pass all except equals and sameBuild
        assertThat(version0).isNotEqualTo(version0_0_0_1bx);
        assertThat(version0_0_0_1.isSameVersionAs(version0_0_0_1bx)).isTrue();
        assertThat(version0_0_0_1.isSameBaseVersionAs(version0_0_0_1bx)).isTrue();
        assertThat(version0_0_0_1.isSameFunctionalVersionAs(version0_0_0_1bx)).isTrue();
        assertThat(version0_0_0_1.isSameBuildAs(version0_0_0_1bx)).isFalse();
    }

    @Test
    public void isLaterThan() throws Exception {

        //baseVersion
        assertThat(version0.isLaterBaseVersionThan(version0a)).isFalse();
        assertThat(version1.isLaterBaseVersionThan(version0)).isTrue();
        assertThat(version0_1.isLaterBaseVersionThan(version0)).isTrue();
        assertThat(version0_0_1.isLaterBaseVersionThan(version0)).isTrue();
        assertThat(version0_0_0_1.isLaterBaseVersionThan(version0)).isTrue();
        assertThat(version0_0_0_1.isLaterBaseVersionThan(version0_0_0_1bx)).isFalse();
        assertThat(version0rc1.isLaterBaseVersionThan(version0)).isFalse();

        //version
        assertThat(version0.isLaterVersionThan(version0a)).isFalse();
        assertThat(version1.isLaterVersionThan(version0)).isTrue();
        assertThat(version0_1.isLaterVersionThan(version0)).isTrue();
        assertThat(version0_0_1.isLaterVersionThan(version0)).isTrue();
        assertThat(version0_0_0_1.isLaterVersionThan(version0)).isTrue();
        assertThat(version0_0_0_1.isLaterVersionThan(version0_0_0_1bx)).isFalse();
        assertThat(version0rc1.isLaterVersionThan(version0)).isFalse(); // qualifiers make it earlier
        assertThat(version0rc2.isLaterVersionThan(version0rc1)).isFalse();

        //functional version
        assertThat(version0.isLaterFunctionalVersionThan(version0a)).isFalse();
        assertThat(version1.isLaterFunctionalVersionThan(version0)).isTrue();
        assertThat(version0_1.isLaterFunctionalVersionThan(version0)).isTrue();
        assertThat(version0_0_1.isLaterFunctionalVersionThan(version0)).isTrue();
        assertThat(version0_0_2.isLaterFunctionalVersionThan(version0_0_1)).isTrue();
        assertThat(version0_0_0_1.isLaterFunctionalVersionThan(version0)).isFalse();
        assertThat(version0_0_0_1.isLaterFunctionalVersionThan(version0_0_0_1bx)).isFalse();
        assertThat(version0_0_0_2.isLaterFunctionalVersionThan(version0_0_0_1)).isFalse();
        assertThat(version0rc1.isLaterFunctionalVersionThan(version0)).isFalse(); // qualifiers make it earlier
        assertThat(version0rc2.isLaterFunctionalVersionThan(version0rc1)).isFalse();

        //build
        assertThat(version0.isLaterBuildThan(version0a)).isFalse();
        assertThat(version1.isLaterBuildThan(version0)).isTrue();
        assertThat(version0_1.isLaterBuildThan(version0)).isTrue();
        assertThat(version0_0_1.isLaterBuildThan(version0)).isTrue();
        assertThat(version0_0_2.isLaterBuildThan(version0_0_1)).isTrue();
        assertThat(version0_0_0_1.isLaterBuildThan(version0)).isTrue();
        assertThat(version0_0_0_1.isLaterBuildThan(version0_0_0_1bx)).isFalse();
        assertThat(version0_0_0_2.isLaterBuildThan(version0_0_0_1bx)).isTrue();
        assertThat(version0_0_0_1by.isLaterBuildThan(version0_0_0_1bx)).isTrue();
        assertThat(version0rc1.isLaterBuildThan(version0)).isFalse(); // qualifiers make it earlier
        assertThat(version0rc2.isLaterBuildThan(version0rc1)).isFalse();
    }

    @Test
    public void increment() throws Exception {
        VersionNumber versionNumber = new VersionNumber(0, 0, 0, 1, "rc1", "buildx");
        assertThat(versionNumber.incMajor()).isEqualTo(new VersionNumber(1, 0, 0, 0));
        assertThat(versionNumber.incMinor()).isEqualTo(new VersionNumber(0, 1, 0, 0));
        assertThat(versionNumber.incPatch()).isEqualTo(new VersionNumber(0, 0, 1, 0));
        assertThat(versionNumber.incNonFunctional()).isEqualTo(new VersionNumber(0, 0, 0, 2));

    }

    @Test
    public void versionToString() throws Exception {
        assertThat(new VersionNumber().toString()).isEqualTo("0.0.0.0");
        assertThat(new VersionNumber(1).toString()).isEqualTo("1.0.0.0");
        assertThat(new VersionNumber(1, 0, 0, 1, "rc1").toString()).isEqualTo("1.0.0.1-rc1");
        assertThat(new VersionNumber(1, 0, 0, 1, "rc1", "45").toString()).isEqualTo("1.0.0.1-rc1+45");

        Scheme scheme = new Scheme(2, "~", "!");
        assertThat(new VersionNumber(1, 0, 0, 1, "rc1", "45", scheme).toString()).isEqualTo("1.0~rc1!45");
    }

    @Test
    public void parse() throws Exception {
        assertThat(VersionNumberKt.parseFullVersionNumber("0.0.0.1-rc1+buildx")).isEqualTo(new VersionNumber(0, 0, 0, 1, "rc1", "buildx"));
        assertThat(VersionNumberKt.parseFullVersionNumber("1.2.3.1-rc1+buildx")).isEqualTo(new VersionNumber(1, 2, 3, 1, "rc1", "buildx"));
        assertThat(VersionNumberKt.parseFullVersionNumber("0.0.0.1-rc1")).isEqualTo(new VersionNumber(0, 0, 0, 1, "rc1"));
        assertThat(VersionNumberKt.parseFullVersionNumber("0.0.0.1+buildx")).isEqualTo(new VersionNumber(0, 0, 0, 1, "", "buildx"));

        assertThat(VersionNumberKt.parseVersion("0.0.0.1")).isEqualTo(new VersionNumber(0, 0, 0, 1));
        assertThat(VersionNumberKt.parseVersion("0.0.0.1", "rc1")).isEqualTo(new VersionNumber(0, 0, 0, 1, "rc1"));
        assertThat(VersionNumberKt.parseVersion("0.0.0.1", "", "buildx")).isEqualTo(new VersionNumber(0, 0, 0, 1, "", "buildx"));
        assertThat(VersionNumberKt.parseVersion("0.0.0.1", "rc2", "buildx")).isEqualTo(new VersionNumber(0, 0, 0, 1, "rc2", "buildx"));

        Scheme scheme = new Scheme(2, "~", "!");
        assertThat(VersionNumberKt.parseVersion("0.0.0.1", "", "", scheme)).isEqualTo(new VersionNumber(0, 0, 0, 1, "", "", scheme));
        assertThat(VersionNumberKt.parseVersion("0.0.0.1", "rc1", "", scheme)).isEqualTo(new VersionNumber(0, 0, 0, 1, "rc1", "", scheme));
        assertThat(VersionNumberKt.parseVersion("0.0.0.1", "", "buildx", scheme)).isEqualTo(new VersionNumber(0, 0, 0, 1, "", "buildx", scheme));
        assertThat(VersionNumberKt.parseVersion("0.0.0.1", "rc2", "buildx", scheme)).isEqualTo(new VersionNumber(0, 0, 0, 1, "rc2", "buildx", scheme));
    }

    @Test(expected = SchemeException.class)
    public void schemeLimitsDepthMin1() throws Exception {
        Scheme scheme = new Scheme(0);
    }

    @Test(expected = SchemeException.class)
    public void schemeLimitsDepthMax4() throws Exception {
        Scheme scheme = new Scheme(5);
    }
}
