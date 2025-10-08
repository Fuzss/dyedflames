package fuzs.dyedflames.handler;

import fuzs.dyedflames.init.ModRegistry;
import fuzs.dyedflames.world.level.block.FireType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.CombatEntry;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class EntityInsideFireHandler {

    public static void onEndEntityTick(Entity entity) {
        emitFlameParticles(entity);
        if (entity.isOnFire() != ModRegistry.WAS_ON_FIRE_ATTACHMENT_TYPE.getOrDefault(entity, false)) {
            ModRegistry.WAS_ON_FIRE_ATTACHMENT_TYPE.set(entity, entity.isOnFire());
            if (entity.isOnFire()) {
                copyLastFireSourceFromAttacker(entity);
            } else if (!entity.level().isClientSide()) {
                ModRegistry.LAST_FIRE_SOURCE_ATTACHMENT_TYPE.set(entity, null);
            }
        }
    }

    private static void emitFlameParticles(Entity entity) {
        // this is client-side
        if (entity.displayFireAnimation() && entity.getRandom().nextInt(5) == 0) {
            Block block = ModRegistry.LAST_FIRE_SOURCE_ATTACHMENT_TYPE.getOrDefault(entity, Blocks.FIRE);
            FireType.getFireType(block)
                    .flatMap(FireType::particleType)
                    .ifPresent((SimpleParticleType simpleParticleType) -> entity.level()
                            .addParticle(simpleParticleType,
                                    entity.getRandomX(0.5),
                                    entity.getRandomY(),
                                    entity.getRandomZ(0.5),
                                    0.0,
                                    0.0,
                                    0.0));
        }
    }

    private static void copyLastFireSourceFromAttacker(Entity entity) {
        if (entity instanceof LivingEntity livingEntity) {
            Entity lastAttacker = getLastAttacker(livingEntity);
            if (lastAttacker != null && lastAttacker.isOnFire()) {
                Block block = ModRegistry.LAST_FIRE_SOURCE_ATTACHMENT_TYPE.get(lastAttacker);
                if (block != null) {
                    ModRegistry.LAST_FIRE_SOURCE_ATTACHMENT_TYPE.set(entity, block);
                }
            }
        }
    }

    @Nullable
    private static Entity getLastAttacker(LivingEntity livingEntity) {
        // if this ever is not a good idea any more, switch to capturing the last attacker manually
        List<CombatEntry> combatEntries = livingEntity.getCombatTracker().entries;
        if (!combatEntries.isEmpty()) {
            CombatEntry combatEntry = combatEntries.getLast();
            return combatEntry.source().getDirectEntity();
        } else {
            // the combat tracker does not record a mob if it did not cause any damage, but those attacks can still set the target on fire,
            // so use this as a fallback
            return livingEntity.getLastHurtByMob();
        }
    }

    public static void setLastFireSourceFromBlocks(Entity entity) {
        // also run this on the client to not have a delay for the correct flame overlay color to show
        checkInsideBlocks(entity, (BlockState blockState) -> {
            return setLastFireSourceFromBlock(entity, blockState);
        });
    }

    public static boolean setLastFireSourceFromBlock(Entity entity, BlockState blockState) {
        Optional<FireType> fireType = FireType.getFireType(blockState.getBlock());
        if (fireType.isPresent()) {
            Optional<TagKey<Fluid>> fluids = fireType.get().fluid();
            if (fluids.isEmpty() || entity.getFluidHeight(fluids.get()) > 0.0) {
                ModRegistry.LAST_FIRE_SOURCE_ATTACHMENT_TYPE.set(entity, blockState.getBlock());
                return true;
            }
        }
        return false;
    }

    private static void checkInsideBlocks(Entity entity, Predicate<BlockState> predicate) {
        List<BlockPos> positions = new ArrayList<>();
        checkInsideBlocks(entity, (Consumer<BlockPos>) positions::add);
        positions.sort(Comparator.comparingDouble((BlockPos blockPos) -> {
            return blockPos.distToCenterSqr(entity.getX(), entity.getY(), entity.getZ());
        }));
        for (BlockPos blockPos : positions) {
            if (predicate.test(entity.level().getBlockState(blockPos))) {
                break;
            }
        }
    }

    /**
     * @see Entity#checkInsideBlocks(List, InsideBlockEffectApplier.StepBasedCollector)
     */
    private static void checkInsideBlocks(Entity entity, Consumer<BlockPos> consumer) {
        if (entity.isAlive() && entity.isAffectedByBlocks()) {
            AABB aABB = entity.getBoundingBox();
            BlockPos fromBlockPos = BlockPos.containing(aABB.minX + 1.0E-5, aABB.minY + 1.0E-5, aABB.minZ + 1.0E-5);
            BlockPos toBlockPos = BlockPos.containing(aABB.maxX - 1.0E-5, aABB.maxY - 1.0E-5, aABB.maxZ - 1.0E-5);
            if (entity.level().hasChunksAt(fromBlockPos, toBlockPos)) {
                BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
                for (int i = fromBlockPos.getX(); i <= toBlockPos.getX(); i++) {
                    for (int j = fromBlockPos.getY(); j <= toBlockPos.getY(); j++) {
                        for (int k = fromBlockPos.getZ(); k <= toBlockPos.getZ(); k++) {
                            consumer.accept(mutableBlockPos.set(i, j, k).immutable());
                        }
                    }
                }
            }
        }
    }
}
