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

export const urlByLevel = showUrlByLevel;
export const getTime = getCTime;
export const r_history = record_history;
export const g_history = get_history;
export const m_history = remove_history;
export const f_d = filter_duplicate;