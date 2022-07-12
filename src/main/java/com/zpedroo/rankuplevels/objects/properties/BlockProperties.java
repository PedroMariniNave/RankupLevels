package com.zpedroo.rankuplevels.objects.properties;

import lombok.Data;
import org.bukkit.Material;

@Data
public class BlockProperties {

    private final Material material;
    private final double expAmount;
}