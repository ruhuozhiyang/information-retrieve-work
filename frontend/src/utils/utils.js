import { g_session, r_session, s_session } from "./session";

const showUrlByLevel = (url) => {
	if (!url) {
		return
	}
	let l_1, l_2, l_3;
	const c = url.split('/');
	const c_p = c.filter((e) => e !== '');
	if (c_p.length >= 2) {
		l_1 = c_p[0] + '//' + c_p[1];
	}
	if (c_p.length >= 3) {
		l_2 = ' > ' + c_p[2];
	}
	if (c_p.length >= 4) {
		for (let i = 3; i < c_p.length; i++) {
			if (c_p[i].indexOf('.html') > -1) {
				l_3 = ' > '
				if (i > 3) {
					l_3 += c_p[i - 1] + ' / ';
				}
				l_3 += c_p[i];
			}
		}
	}
	return l_1 + l_2 + l_3;
}

const getCTime = (time, flag) => {
	let t = trim(time);
	if (flag === '-') {
		let temp = '';
		let appendix = ['年', '月', '日'];
		let t_l = t.split(' ')[0].split('-');
		for (let i = 0; i < t_l.length; i++) {
			temp += t_l[i] + appendix[i];
		}
		t = temp;
	}
	return t;
}

const trim = (s) => {
	if (s == null) return "" ;
	while ( s.charAt(0)  == ' ') {
		s = s.substring(1, s.length);
	}
	while (s.charAt(s.length - 1)  == ' ') {
		s = s.substring(0, s.length - 1);
	}
	return s;
}

const record_history = (k, v) => {
	let c_h = g_session(k);
	if (!c_h) {
		let t = [];
		t.push(v)
		s_session(k, t);
		return
	}
	if (c_h.indexOf(v) > -1) {
		return
	}
	r_session(k);
	c_h.push(v);
	s_session(k, c_h);
	return
}

const get_history = (k) => {
	return g_session(k);
}

const remove_history = (k, v) => {
	let c_h = g_session(k);
	let index = c_h.indexOf(v);
	c_h.splice(index, 1);
	r_session(k);
	if (c_h.length > 0) {
		s_session(k, c_h);
	}
}

const filter_duplicate = (arr) => {
	return [... new Set(arr)]
}

const get_time = () => {
	let y = new Date().getFullYear();
	let m = new Date().getMonth() + 1;
	let d = new Date().getDate();
	return y + '-' + m + '-' + d;
}

/**
 * 自定义高亮显示.
 * @param {*} c 高亮字段.
 * @param {*} arr 候选内容数组.
 * @param {*} l_t 左标签.
 * @param {*} r_t 右标签.
 * @returns [].
 */
const high_light_arr_obj = (c, arr, l_t, r_t) => {
	let r = [];
	if (Array.isArray(arr)) {
		arr.forEach(e => {
			if (e.indexOf(c) === 0) {
				let l_i = e.indexOf(c);
				let r_i = e.indexOf(c) + c.length;
				r.push(e.substring(0, l_i) + l_t + e.substring(l_i, r_i) + r_t + e.substring(r_i, e.length));
			}
		});
	}
	return r
}

export const urlByLevel = showUrlByLevel;
export const getTime = getCTime;
export const r_history = record_history;
export const g_history = get_history;
export const m_history = remove_history;
export const f_d = filter_duplicate;
export const g_t = get_time;
export const h_l_a_o = high_light_arr_obj;