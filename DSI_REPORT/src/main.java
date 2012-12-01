import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

public class main {

	private static String[][] instructors = new String[100][30];
	private static String[][] course = new String[100][30];
	private static String[][] teaches = new String[100][30];

	private static Connection conn;

	public static void readdata() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager
					.getConnection("jdbc:mysql://localhost:3306/database",
							"root", "buzhidao");

			Statement stmt = conn.createStatement();

			ResultSet rs = stmt.executeQuery("select * from instructor");
			int row = 1;
			ResultSetMetaData rsmd = rs.getMetaData();
			int i = 0;
			int count = rsmd.getColumnCount();
			// get column name
			for (i = 0; i < count; i++) {
				instructors[0][i] = rsmd.getColumnName(i + 1);
			}
			while (rs.next()) {
				for (i = 0; i < count; i++) {
					instructors[row][i] = rs.getString(i + 1);
				}
				row++;
			}

			rs = stmt.executeQuery("select * from course");
			row = 1;
			rsmd = rs.getMetaData();
			i = 0;
			count = rsmd.getColumnCount();
			// get column name
			for (i = 0; i < count; i++) {
				course[0][i] = rsmd.getColumnName(i + 1);
			}
			while (rs.next()) {
				for (i = 0; i < count; i++) {
					course[row][i] = rs.getString(i + 1);
				}
				row++;
			}

			rs = stmt.executeQuery("select * from teaches");
			row = 1;
			rsmd = rs.getMetaData();
			i = 0;
			count = rsmd.getColumnCount();
			// get column name
			for (i = 0; i < count; i++) {
				teaches[0][i] = rsmd.getColumnName(i + 1);
			}
			while (rs.next()) {
				for (i = 0; i < count; i++) {
					teaches[row][i] = rs.getString(i + 1);
				}
				row++;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static int AttributeOrder(String[][] table, String value) {
		int k = -1, j = 0;
		while (table[0][j] != null) {
			if (table[0][j].equals(value))
				k = j;
			j++;
		}
		return k;
	}

	public static String[][] project(String[][] table, String value1,
			String value2) {
		int i = 1;
		String[][] result = new String[100][30];
		int m = AttributeOrder(table, value1);
		int n = AttributeOrder(table, value2);
		result[0][0] = value1;
		result[0][1] = value2;

		while (table[i][0] != null) {
			result[i][0] = table[i][m];
			result[i][1] = table[i][n];
			i++;
		}
		return result;

	}

	public static String[][] select(String[][] table, String label, String value) {
		int i = 1, j = 0;
		String[][] result = new String[100][30];

		int k = AttributeOrder(table, label);

		while (table[0][j] != null) {
			result[0][j] = table[0][j];
			j++;
		}
		j = 0;
		int next = 1;

		while (table[i][0] != null) {
			if (table[i][k].equals(value)) {
				while (table[i][j] != null) {
					result[next][j] = table[i][j];
					j++;
				}
				next++;
			}
			i++;
		}
		return result;
	}

	public static String[][] join(String[][] a1, String[][] a2) {

		String[][] result = new String[100][30];
		int a = 1, m = 0, n = 0;
		int i = 1, j = 0;

		while (a1[0][j] != null) {
			n = AttributeOrder(a2, a1[0][j]);

			if (n != -1) {
				m = j;
				break;
			}
			j++;
		}

		j = 0;
		while (a1[0][j] != null) {
			result[0][j] = a1[0][j];
			j++;
		}

		int next = j;
		j = 0;
		while (a2[0][j] != null) {
			if (j != n) {
				result[0][next] = a2[0][j];
				next++;
			}
			j++;

		}
		j = 0;
		int other = 1;
		while (a1[i][m] != null) {
			while (a2[a][n] != null) {
				if (a1[i][m].equals(a2[a][n])) {
					while (a1[i][j] != null) {
						result[other][j] = a1[i][j];
						j++;
					}
					next = j;
					j = 0;
					while (a2[a][j] != null) {
						if (j != n) {
							result[other][next] = a2[a][j];
							next++;
						}
						j++;
					}
					j = 0;
					other++;
				}

				a++;
			}
			a = 0;
			i++;
		}

		return result;
	}

	public static void main(String[] args) {

		readdata();
		int i = 0, j = 0;

		String[][] result = new String[100][30];
		result = project(join(join(select(instructors, "dept_name", "Biology"),
				teaches), project(course, "course_id", "title")), "name",
				"title");

		System.out.println("Result:");
		System.out.println("-------------------------------");

		while (result[i][j] != null) {
			while (result[i][j] != null) {
				System.out.print(result[i][j] + "  |  ");
				j++;
			}
			System.out.println("\n-------------------------------");
			i++;
			j = 0;
		}

	}
}
