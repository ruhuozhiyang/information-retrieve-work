const showUrlByLevel = (url) => {
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

export const urlByLevel = showUrlByLevel;