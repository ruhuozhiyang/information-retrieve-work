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
				l_3 = ' > ' + c_p[i];
			}
		}
	}
	return l_1 + l_2 + l_3;
}

const getCTime = (time, flag) => {
	let t = time;
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

export const urlByLevel = showUrlByLevel;
export const getTime = getCTime;