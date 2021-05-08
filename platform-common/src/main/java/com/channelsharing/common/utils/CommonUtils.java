package com.channelsharing.common.utils;

import com.channelsharing.common.entity.BaseEntity;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.*;

public class CommonUtils {

	public static List<Long> strToLongList(String params, String separatorChars) {
		if (StringUtils.isNotBlank(params)) {
			String[] temp = StringUtils.split(params, separatorChars);
			List<Long> result = new ArrayList(temp.length);
			for (String str : temp) {
				result.add(Long.valueOf(str));
			}

			return result;
		} else {
			return Lists.newArrayList();
		}
	}

	public static List<String> stringToList(String params, String separatorChars) {
		if (StringUtils.isNotBlank(params)) {
			String[] temp = StringUtils.split(params, separatorChars);
			List<String> list = Arrays.asList(temp);

			return list;
		} else {
			return Lists.newArrayList();
		}
	}

	public static <T extends BaseEntity> List<T> sortByIds(List<T> toSortList, List<Long> ids) {
		if (!CollectionUtils.isEmpty(toSortList)) {
			Map<Long, T> tempMap = new HashMap(toSortList.size());
			for (T item : toSortList) {
				if (item != null)
					tempMap.put(item.getId(), item);
			}

			List<T> result = new ArrayList(toSortList.size());
			for (Long id : ids) {
				if (tempMap.get(id) != null)
					result.add(tempMap.get(id));
			}

			return result;
		}

		return Lists.newArrayList();
	}

	public static boolean IsIpv4(String ipv4) {
		if (ipv4 == null || ipv4.length() == 0) {
			return false;// 字符串为空或者空串
		}
		String[] parts = StringUtils.split(ipv4, ".");
		if (parts.length != 4) {
			return false;// 分割开的数组根本就不是4个数字
		}
		for (int i = 0; i < parts.length; i++) {
			try {
				int n = Integer.parseInt(parts[i]);
				if (n < 0 || n > 255) {
					return false;// 数字不在正确范围内
				}
			} catch (NumberFormatException e) {
				return false;// 转换数字不正确
			}
		}
		return true;
	}
}
