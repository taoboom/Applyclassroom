package com.tygy.bishe.common;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.UpdateChainWrapper;
import org.springframework.transaction.annotation.Transactional;
 
import java.util.List;
 
public interface BaseSqlMapper<T> extends BaseMapper<T> {
 
    /**
     * 默认批次提交数量
     */
    int DEFAULT_BATCH_SIZE = 1000;
 
    /**
     * 以下定义的 4个 default method, copy from {@link com.baomidou.mybatisplus.extension.toolkit.ChainWrappers}
     */
    default QueryChainWrapper<T> queryChain() {
        return new QueryChainWrapper<>(this);
    }
 
    default LambdaQueryChainWrapper<T> lambdaQueryChain() {
        return new LambdaQueryChainWrapper<>(this);
    }
 
    default UpdateChainWrapper<T> updateChain() {
        return new UpdateChainWrapper<>(this);
    }
 
    default LambdaUpdateChainWrapper<T> lambdaUpdateChain() {
        return new LambdaUpdateChainWrapper<>(this);
    }
 
    /**
     * 批量新增数据,自选字段 insert. 自动按每批1000插入数据库
     * 此填充不会填充 FieldFill.UPDATE 的字段。
     * 注意数据库默认更新的字段也需要手工设置
     *
     * @see MySqlInjector#getMethodList(Class)
     * @param entityList 数据
     * @return 插入条数
     */
    @Transactional(rollbackFor = Exception.class)
    default int insertBatch(List<T> entityList) {
        return this.insertBatchSomeColumn(entityList, DEFAULT_BATCH_SIZE);
    }
 
    /**
     * 批量新增数据,自选字段 insert
     * 不会分批插入，需要分批请调用方法insertBatch或者 insertBatchSomeColumn(List<T> entityList, int size)
     * 此填充不会填充 FieldFill.UPDATE 的字段。
     * 注意数据库默认更新的字段也需要手工设置
     *
     * @see MySqlInjector#getMethodList(Class)
     * @param entityList 数据
     * @return 插入条数
     */
    int insertBatchSomeColumn(List<T> entityList);
 
    /**
     * 分批插入。每次插入
     * @param entityList 原实体对象
     * @param size 分批大小
     * @return 总插入记录
     */
    @Transactional(rollbackFor = Exception.class)
    default int insertBatchSomeColumn(List<T> entityList, int size) {
        if (CollUtil.isEmpty(entityList)) {
            return 0;
        }
        List<List<T>> split = CollUtil.split(entityList, size);
        return split.stream().mapToInt(this::insertBatchSomeColumn).sum();
 
    }
 
}