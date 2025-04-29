package fuzs.dyedflames.handler;

import fuzs.dyedflames.init.ModRegistry;
import fuzs.dyedflames.world.level.block.FireType;
import fuzs.puzzleslib.api.event.v1.core.EventResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.CombatEntry;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
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

    public static EventResult onStartEntityTick(Entity entity) {

        if (!entity.level().isClientSide) {

            if (triggerIsOnFireChange(entity, false)) {

                if (entity instanceof LivingEntity livingEntity) {

                    Entity lastAttacker = getLastAttacker(livingEntity);
                    if (lastAttacker != null && lastAttacker.isOnFire()) {
                        Block block = ModRegistry.LAST_FIRE_SOURCE_ATTACHMENT_TYPE.get(lastAttacker);
                        if (block != null) {
                            ModRegistry.LAST_FIRE_SOURCE_ATTACHMENT_TYPE.set(entity, block);
                        }
                    }
                }

                if (entity instanceof Player) {
                    ModRegistry.WAS_PLAYER_ON_FIRE_ATTACHMENT_TYPE.set(entity, true);
                }
            }

            // just always check this, does not hurt
            clearLastFireSource(entity);
        }

        return EventResult.PASS;
    }

    public static void onEndEntityTick(Entity entity) {

        if (entity.isOnFire()) {

            // also run this on the client to not have a delay for the correct flame overlay color to show
            checkInsideBlocks(entity, (BlockState blockState) -> {
                Optional<FireType> fireType = FireType.getFireType(blockState.getBlock());
                if (fireType.isPresent()) {
                    Optional<TagKey<Fluid>> fluids = fireType.get().fluid();
                    if (fluids.isEmpty() || entity.getFluidHeight(fluids.get()) > 0.0) {
                        ModRegistry.LAST_FIRE_SOURCE_ATTACHMENT_TYPE.set(entity, blockState.getBlock());
                        return true;
                    }
                }

                return false;
            });
        }

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

        if (!entity.level().isClientSide) {

            // just always check this, does not hurt
            clearLastFireSource(entity);
        }
    }

    private static void clearLastFireSource(Entity entity) {
        if (triggerIsOnFireChange(entity, true)) {
            ModRegistry.LAST_FIRE_SOURCE_ATTACHMENT_TYPE.set(entity, null);
            if (entity instanceof Player) {
                ModRegistry.WAS_PLAYER_ON_FIRE_ATTACHMENT_TYPE.set(entity, false);
            }
        }
    }

    private static boolean triggerIsOnFireChange(Entity entity, boolean wasOnFire) {
        // handle player separately, otherwise there is a brief flicker when extinguishing back to the default fire color
        // using this approach for other mobs as well results in the same behavior for those
        if (entity instanceof Player) {
            return wasOnFire == ModRegistry.WAS_PLAYER_ON_FIRE_ATTACHMENT_TYPE.getOrDefault(entity, false) &&
                    wasOnFire != entity.getSharedFlag(0);
        } else {
            return wasOnFire == entity.wasOnFire && wasOnFire != entity.isOnFire();
        }
    }

    @Nullable
    public static Entity getLastAttacker(LivingEntity livingEntity) {

        // if this ever is not a good idea anymore switch to capturing the last attacker in
        List<CombatEntry> combatEntries = livingEntity.getCombatTracker().entries;
        if (!combatEntries.isEmpty()) {
            CombatEntry combatEntry = combatEntries.getLast();
            return combatEntry.source().getDirectEntity();
        }

        // the combat tracker does not record a mob if it did not cause any damage, but those attacks can still set the target on fire,
        // so use this as a fallback
        return livingEntity.getLastHurtByMob();
    }

    private static void checkInsideBlocks(Entity entity, Predicate<BlockState> predicate) {

        List<BlockPos> positions = new ArrayList<>();
        checkInsideBlocks(entity, (Consumer<BlockPos>) positions::add);

        positions.sort(Comparator.comparingDouble(entry -> {
            return entry.distToCenterSqr(entity.getX(), entity.getY(), entity.getZ());
        }));

        for (BlockPos blockPos : positions) {
            if (predicate.test(entity.level().getBlockState(blockPos))) {
                break;
            }
        }
    }

    /**
     * Copied from {@link Entity#checkInsideBlocks()}.
     */
    private static void checkInsideBlocks(Entity entity, Consumer<BlockPos> consumer) {

        if (entity.isAlive()) {

            AABB aABB = entity.getBoundingBox();
            BlockPos blockPos = BlockPos.containing(aABB.minX + 1.0E-7, aABB.minY + 1.0E-7, aABB.minZ + 1.0E-7);
            BlockPos blockPos2 = BlockPos.containing(aABB.maxX - 1.0E-7, aABB.maxY - 1.0E-7, aABB.maxZ - 1.0E-7);
            if (entity.level().hasChunksAt(blockPos, blockPos2)) {
                BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();

                for (int i = blockPos.getX(); i <= blockPos2.getX(); i++) {
                    for (int j = blockPos.getY(); j <= blockPos2.getY(); j++) {
                        for (int k = blockPos.getZ(); k <= blockPos2.getZ(); k++) {
                            consumer.accept(mutableBlockPos.set(i, j, k).immutable());
                        }
                    }
                }
            }
        }
    }
}
