import { Divider } from "antd";
import styles from "styles/client.module.scss";
import SearchClient from "@/components/client/search.client";
import PostCard from "@/components/client/card/post.card";
import CompanyCard from "@/components/client/card/company.card";

const HomePage = () => {
  return (
    <>
      <div className={styles["homepage-wrapper"]}>
        {/* Hero Section */}
        <div className={styles["hero-section"]}>
          <div className={styles["hero-overlay"]}>
            <div className={`${styles["container"]} ${styles["hero-content"]}`}>
              <div className={styles["hero-text"]}>
                <h1 className={styles["hero-title"]}>
                  Job<span className={styles["hero-highlight"]}>Hunter</span>
                </h1>
                <p className={styles["hero-subtitle"]}>
                  Nền tảng tuyển dụng hàng đầu - Kết nối nhà tuyển dụng và ứng
                  viên
                </p>
                <p className={styles["hero-description"]}>
                  Tìm kiếm công việc mơ ước hoặc tuyển dụng nhân tài phù hợp
                </p>
              </div>

              {/* Search in Hero */}
              <div className={styles["hero-search"]}>
                <SearchClient />
              </div>
            </div>
          </div>
        </div>

        {/* Main Content */}
        <div className={`${styles["container"]} ${styles["home-section"]}`}>
          <CompanyCard />
          <div style={{ margin: 50 }}></div>
          <Divider />
          <PostCard />
        </div>
      </div>
    </>
  );
};

export default HomePage;
