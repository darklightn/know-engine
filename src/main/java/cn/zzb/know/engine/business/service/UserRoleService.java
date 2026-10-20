package cn.zzb.know.engine.business.service;

import cn.zzb.know.engine.chat.entity.ChatParam;
import cn.zzb.know.engine.rag.constant.RoleEnum;

public interface UserRoleService {

    public RoleEnum getUserRole(ChatParam chatParam);
}
