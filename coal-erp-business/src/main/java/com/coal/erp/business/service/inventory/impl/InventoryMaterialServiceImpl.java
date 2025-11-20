package com.coal.erp.business.service.inventory.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coal.erp.business.domain.inventory.InventoryMaterial;
import com.coal.erp.business.mapper.inventory.InventoryMaterialMapper;
import com.coal.erp.business.service.inventory.IInventoryMaterialService;
import com.coal.erp.common.core.domain.R;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 库存物品服务实现
 */
@Service
public class InventoryMaterialServiceImpl extends ServiceImpl<InventoryMaterialMapper, InventoryMaterial>
        implements IInventoryMaterialService {
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> createMaterial(InventoryMaterial material) {
        try {
            if (!checkMaterialCodeUnique(material)) {
                return R.error("物料编码已存在");
            }
            
            material.setStatus("ACTIVE");
            material.setCreateTime(new Date());
            material.setUpdateTime(new Date());
            
            save(material);
            return R.success(material);
        } catch (Exception e) {
            return R.error("创建物料失败：" + e.getMessage());
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> updateMaterial(InventoryMaterial material) {
        try {
            if (!checkMaterialCodeUnique(material)) {
                return R.error("物料编码已存在");
            }
            
            material.setUpdateTime(new Date());
            updateById(material);
            return R.success();
        } catch (Exception e) {
            return R.error("更新物料失败：" + e.getMessage());
        }
    }
    
    @Override
    public Page<InventoryMaterial> pageMaterial(Long current, Long size, String materialCode, String materialName, String materialType, String status) {
        Page<InventoryMaterial> page = new Page<>(current, size);
        LambdaQueryWrapper<InventoryMaterial> wrapper = new LambdaQueryWrapper<>();
        
        if (materialCode != null && !materialCode.isEmpty()) {
            wrapper.like(InventoryMaterial::getMaterialCode, materialCode);
        }
        if (materialName != null && !materialName.isEmpty()) {
            wrapper.like(InventoryMaterial::getMaterialName, materialName);
        }
        if (materialType != null && !materialType.isEmpty()) {
            wrapper.eq(InventoryMaterial::getMaterialType, materialType);
        }
        if (status != null && !status.isEmpty()) {
            wrapper.eq(InventoryMaterial::getStatus, status);
        }
        
        wrapper.orderByDesc(InventoryMaterial::getCreateTime);
        return page(page, wrapper);
    }
    
    @Override
    public boolean checkMaterialCodeUnique(InventoryMaterial material) {
        LambdaQueryWrapper<InventoryMaterial> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InventoryMaterial::getMaterialCode, material.getMaterialCode());
        if (material.getMaterialId() != null) {
            wrapper.ne(InventoryMaterial::getMaterialId, material.getMaterialId());
        }
        return count(wrapper) == 0;
    }
}

