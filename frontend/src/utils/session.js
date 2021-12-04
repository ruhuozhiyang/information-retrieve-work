const session = window.localStorage;

const set_session = (k, v) => {
	session.setItem(k, JSON.stringify(v));
};

const get_session = (k) => {
	return JSON.parse(session.getItem(k));
}

const remove_session = (k) => {
  session.removeItem(k);
}

export const s_session = set_session;
export const g_session = get_session;
export const r_session = remove_session;