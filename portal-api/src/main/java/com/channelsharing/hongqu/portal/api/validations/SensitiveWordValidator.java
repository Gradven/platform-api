package com.channelsharing.hongqu.portal.api.validations;

import com.channelsharing.hongqu.portal.api.entity.SensitiveWord;
import com.channelsharing.hongqu.portal.api.service.SensitiveWordService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;


@Component
public class SensitiveWordValidator implements ConstraintValidator<Sensitive, String> {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private static final int DATA_LIMIT = 9999;

	@Resource
	private SensitiveWordService sensitiveWordService;

	private Map<String, Node> sensitiveWordMap = null;

	@Override
	public void initialize(Sensitive constraintAnnotation) {

	}

	public String replaceSensitiveWord(String txt) {
		StringBuilder result = new StringBuilder(64);
		for (int i = 0; i < txt.length(); i++) {
			int length = this.checkSensitiveWord(txt, i); // 判断是否包含敏感字符
			if (length > 0) { // 存在,加入list中
				result.append("**");
				i = i + length - 1; // 减1的原因，是因为for会自增
			} else {
				result.append(txt.substring(i, i + 1));
			}
		}

		return result.toString();
	}

	public String markSensitiveWord(String txt) {
		StringBuilder result = new StringBuilder(64);
		for (int i = 0; i < txt.length(); i++) {
			int length = this.checkSensitiveWord(txt, i); // 判断是否包含敏感字符
			if (length > 0) { // 存在,加入list中
				result.append("<span style='color:#fff;font-weight:bolder;background-color:#f00;'>");
				result.append(txt.substring(i, i + length));
				result.append("</span>");
				i = i + length - 1; // 减1的原因，是因为for会自增
			} else {
				result.append(txt.substring(i, i + 1));
			}
		}

		return result.toString();
	}

	private class Node {
		public Map<String, Node> childNodes = new HashMap<>();
		public boolean isEnd = false;
	}

	private void initSensitiveWordCache(Collection<String> sensitiveWordCollection) {
		Map<String, Node> sensitiveWordsMap = new HashMap<>(sensitiveWordCollection.size());

		for (String key : sensitiveWordCollection) {
			String nodeKey = String.valueOf(key.charAt(0)).toLowerCase();
			Node rootNode = sensitiveWordsMap.get(nodeKey);
			if (rootNode == null) {
				rootNode = new Node();
				sensitiveWordsMap.put(nodeKey, rootNode);
			}

			int length = key.length();
			if (length == 1) {
				rootNode.isEnd = true;
			} else {
				Node parentNode = rootNode;
				Node childNode = null;
				for (int i = 1; i < length; i++) {
					nodeKey = String.valueOf(key.charAt(i)).toLowerCase();

					childNode = parentNode.childNodes.get(nodeKey);
					if (childNode == null) {
						childNode = new Node();
						if (i == (length - 1)) {
							childNode.isEnd = true;
						}

						parentNode.childNodes.put(nodeKey, childNode);
					}

					parentNode = childNode;
				}
			}
		}

		this.sensitiveWordMap = sensitiveWordsMap;
	}

	private void initSensitiveWordCache() {
		SensitiveWord sensitiveWord = new SensitiveWord();
		sensitiveWord.setLimit(DATA_LIMIT);
		Collection<SensitiveWord > sensitiveWordsCollection = sensitiveWordService.findPaging(sensitiveWord).getRows();

		Collection<String> sensitiveStrCollection = new HashSet<>();
		for (SensitiveWord  word : sensitiveWordsCollection){
			sensitiveStrCollection.add(word.getWord());
		}

		if (CollectionUtils.isNotEmpty(sensitiveStrCollection)) {
			this.initSensitiveWordCache(sensitiveStrCollection);
		} else {
			logger.warn("sensitive word is empty");
		}
	}



	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (StringUtils.isBlank(value)) {
			return true;
		}

		return !isContainSensitiveWord(value);
	}

	private boolean isContainSensitiveWord(String txt) {
		for (int i = 0; i < txt.length(); i++) {
			int matchFlag = this.checkSensitiveWord(txt, i); // 判断是否包含敏感字符
			if (matchFlag > 0) { // 大于0存在，返回true
				return true;
			}
		}
		return false;
	}

	private int checkSensitiveWord(String txt, int beginIndex) {
		boolean flag = false; // 敏感词结束标识位：用于敏感词只有1位的情况
		int matchFlag = 0; // 匹配标识数默认为0
		char word = 0;

		if (sensitiveWordMap == null) {
			this.initSensitiveWordCache();
		}

		Node currentNode = sensitiveWordMap.get(String.valueOf(txt.charAt(beginIndex)).toLowerCase());
		if (currentNode == null) {
			return matchFlag;
		} else if (currentNode.isEnd) {
			matchFlag++;
			return matchFlag;
		} else {
			matchFlag++;
			for (int i = beginIndex + 1; i < txt.length(); i++) {
				word = txt.charAt(i);
				String nodeKey = String.valueOf(word).toLowerCase();
				currentNode = currentNode.childNodes.get(nodeKey); // 获取指定key
				if (currentNode != null) { // 存在，则判断是否为最后一个
					if (matchFlag == 0 && !isFirstLetterInWord(word, txt, i)) {// 判断是否是单词首字母。(如果lish是敏感词汇的话，English不能被匹配到)
						break;
					}

					matchFlag++; // 找到相应key，匹配标识+1
					if (currentNode.isEnd) { // 如果为最后一个匹配规则，结束循环，返回匹配标识数
						flag = true;
						if (!isLastLetterInWord(word, txt, i)) {// 判断是否是单词结束字母。(如果Eng是敏感词汇的话，English不能被匹配到)
							flag = false;
						}

						break;
					}
				} else { // 不存在，直接返回
					break;
				}
			}

			if (matchFlag < 2 || !flag) { // 长度必须大于等于2，为词
				matchFlag = 0;
			}

			return matchFlag;
		}
	}

	private boolean isFirstLetterInWord(char chr, String txt, int i) {
		if (i == 0) {
			return true;
		}
		if (isEnglishLetter(chr)) {
			char ch = txt.charAt(i - 1);
			if (isEnglishLetter(ch)) {
				return false;
			}
		}
		return true;
	}

	private boolean isLastLetterInWord(char chr, String txt, int i) {
		int len = txt.length();
		if (i >= (len - 1)) {
			return true;
		}
		if (isEnglishLetter(chr)) {
			char ch = txt.charAt(i + 1);
			if (isEnglishLetter(ch)) {
				return false;
			}
		}
		return true;
	}

	private boolean isEnglishLetter(char ch) {
		return (ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z');
	}
}
