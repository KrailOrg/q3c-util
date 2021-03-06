/*
 *
 *  * Copyright (c) 2016. David Sowerby
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 *  * the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 *  * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 *  * specific language governing permissions and limitations under the License.
 *
 */

package uk.q3c.util.forest;

import org.junit.Before;
import org.junit.Test;

import java.util.Comparator;

import static com.google.common.base.Preconditions.*;
import static org.assertj.core.api.Assertions.*;

public class TargetTreeWrapper_BasicForestTest {
    private TestCaptionReader captionReader;
    private BasicForest<TargetTestNode> forest;
    private SourceTestNode sourceNodeA;
    private SourceTestNode sourceNodeB;
    // private SourceTestNode sourceNodeC;
    // private SourceTestNode sourceNodeD;
    private TargetTestNode targetNodeA;
    private TargetTestNode targetNodeB;
    private TargetTreeWrapper_BasicForest<SourceTestNode, TargetTestNode> wrapper;

    @Before
    public void setup() {
        forest = new BasicForest<>();
        sourceNodeA = new SourceTestNode("sa");
        sourceNodeB = new SourceTestNode("sb");
        // sourceNodeC = new SourceTestNode("sc");
        // sourceNodeD = new SourceTestNode("sd");
        wrapper = new TargetTreeWrapper_BasicForest<>(forest);
        wrapper.setNodeModifier(new TestNodeModifier());
        wrapper.setCaptionReader(captionReader);
    }

    @Test(expected = NullPointerException.class)
    public void createNode_nullChild() {

        // given
        // when
        wrapper.createNode(targetNodeA, null);
        // then
    }

    @Test
    public void createNode() {

        // given

        // when
        TargetTestNode result = wrapper.createNode(null, sourceNodeA);
        // then
        assertThat(result).isNotNull();
        assertThat(result).isInstanceOf(TargetTestNode.class);
        assertThat(result.name).isEqualTo(sourceNodeA.name);
    }

    @Test(expected = TreeCopyException.class)
    public void sort() {

        // given
        // when
        wrapper.sortChildren(targetNodeA, null);
        // then
    }

    @Test
    public void addChild() {

        // given
        targetNodeA = wrapper.createNode(null, sourceNodeA);
        targetNodeB = wrapper.createNode(null, sourceNodeB);
        // when
        wrapper.addChild(targetNodeA, targetNodeB);
        // then
        assertThat(forest.getParent(targetNodeB)).isEqualTo(targetNodeA);
    }

    static class TargetTestNode {
        SourceTestNode sourceNode;
        String name;

        public TargetTestNode(String name) {
            super();
            this.name = name;
        }
    }

    static class SourceTestNode {
        String name;

        public SourceTestNode(String name) {
            super();
            this.name = name;
        }
    }

    static class TestNodeModifier implements NodeModifier<SourceTestNode, TargetTestNode> {

        @Override
        public TargetTestNode create(TargetTestNode parentNode, SourceTestNode sourceNode) {
            checkNotNull(sourceNode);
            TargetTestNode newNode = new TargetTestNode(sourceNode.name);
            newNode.sourceNode = sourceNode;
            return newNode;
        }

        @Override
        public boolean attachOnCreate() {
            return false;
        }

        @Override
        public SourceTestNode sourceNodeFor(TargetTestNode targetNode) {
            checkNotNull(targetNode);
            return targetNode.sourceNode;
        }

        @Override
        public void setLeaf(TargetTestNode targetNode) {
            // do nothing

        }

        @Override
        public void forceSetLeaf(TargetTestNode targetNode) {

        }

        @Override
        public void setCaption(TargetTestNode targetNode, String caption) {
            targetNode.name = caption;
        }

        @Override
        public void sortChildren(TargetTestNode parentNode, Comparator<TargetTestNode> comparator) {

        }

    }

    static class TestCaptionReader implements CaptionReader<SourceTestNode> {

        @Override
        public String getCaption(SourceTestNode sourceNode) {
            return sourceNode.name;
        }

    }
}
