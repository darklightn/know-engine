package cn.zzb.know.engine.business.mapper;

import cn.zzb.know.engine.business.entity.MyCar;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 我的车辆信息 Mapper 接口
 */
@Mapper
public interface MyCarMapper extends BaseMapper<MyCar> {
}
