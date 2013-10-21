int main(int argc, char* argv[])
{
	String word;
	ofstream of("out.txt");
	while(cin >> word)
	{
		of << "<KeyWord name=\"" << word << "\" />" << endl;
	}
	cin.ignore(2);
	of.close();
	//这里是注释块
	cout << sizeof(1 < 2) << endl;
	return 0;
}
