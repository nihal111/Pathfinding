/*
 * Copyright 2015 MovingBlocks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.terasology.minion.move;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terasology.logic.behavior.tree.DecoratorNode;
import org.terasology.logic.behavior.tree.Status;
import org.terasology.logic.location.LocationComponent;
import org.terasology.math.geom.Vector3f;
import org.terasology.navgraph.WalkableBlock;
import org.terasology.pathfinding.componentSystem.PathRenderSystem;
import org.terasology.pathfinding.model.Path;
import org.terasology.registry.In;

/**
 * Call child node, as long as the actor has not reached the end of the path. Sets <b>MinionMoveComponent.target</b> to next step in path.<br/>
 * <br/>
 * <b>SUCCESS</b>: when actor has reached end of path.<br/>
 * <b>FAILURE</b>: if no path was found previously.<br/>
 * <br/>
 * Auto generated javadoc - modify README.markdown instead!
 */
public class MoveAlongPathNode extends DecoratorNode {
    public MoveAlongPathNode() {
    }

    @Override
    public MoveAlongPathTask createTask() {
        return new MoveAlongPathTask(this);
    }

    public static class MoveAlongPathTask extends DecoratorNode.DecoratorTask {
        private static final Logger logger = LoggerFactory.getLogger(MoveAlongPathNode.class);
        private Path path;
        private int currentIndex;
        @In
        private PathRenderSystem pathRenderSystem;

        public MoveAlongPathTask(MoveAlongPathNode node) {
            super(node);
        }

        @Override
        public void onInitialize() {
            MinionMoveComponent moveComponent = actor().getComponent(MinionMoveComponent.class);
            if (moveComponent != null && moveComponent.path != null && moveComponent.path != Path.INVALID) {
                path = moveComponent.path;
                pathRenderSystem.addPath(path);
                currentIndex = 0;
                WalkableBlock block = path.get(currentIndex);
                logger.info("Start moving along path to step " + currentIndex + " " + block.getBlockPosition());
                moveComponent.target = block.getBlockPosition().toVector3f();
                actor().save(moveComponent);

                start(getNode().child);
            }
        }

        @Override
        public Status update(float dt) {
            return path != null ? Status.RUNNING : Status.FAILURE;
        }

        @Override
        public void handle(Status result) {
            if (result == Status.FAILURE) {
                logger.info("FAILURE abort moving along path ");
                stop(Status.FAILURE);
                return;
            }
            currentIndex++;
            if (currentIndex < path.size()) {
                WalkableBlock block = path.get(currentIndex);
                logger.info(" Continue moving along path to step " + currentIndex + " " + block.getBlockPosition());
                MinionMoveComponent moveComponent = actor().getComponent(MinionMoveComponent.class);
                Vector3f pos = block.getBlockPosition().toVector3f();
                pos.add(new Vector3f(0, 1, 0));
                moveComponent.target = pos;
                actor().save(moveComponent);
                start(getNode().child);
            } else {
                pathRenderSystem.removePath(path);
                LocationComponent locationComponent = actor().getComponent(LocationComponent.class);
                MinionMoveComponent moveComponent = actor().getComponent(MinionMoveComponent.class);
                logger.info("Finished moving along path pos = " + locationComponent.getWorldPosition() + " block = " + moveComponent.currentBlock);
                stop(Status.SUCCESS);
            }
        }

        @Override
        public MoveAlongPathNode getNode() {
            return (MoveAlongPathNode) super.getNode();
        }
    }

}
